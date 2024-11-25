/*
 * Copyright 2018 Karlsruhe Institute of Technology.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.kit.datamanager.indexer.messagehandler;

import edu.kit.datamanager.entities.messaging.BasicMessage;
import edu.kit.datamanager.messaging.client.handler.IMessageHandler;
import edu.kit.datamanager.messaging.client.util.MessageHandlerUtils;
import edu.kit.datamanager.indexer.consumer.IConsumerEngine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.jknack.handlebars.Handlebars;
import edu.kit.datamanager.indexer.configuration.ApplicationProperties;
import edu.kit.datamanager.indexer.exception.IndexerException;
import edu.kit.datamanager.indexer.service.impl.IndexingService;
import edu.kit.datamanager.indexer.service.impl.MappingService;
import edu.kit.datamanager.indexer.util.ElasticsearchUtil;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andreas Pfeil
 */
@Component
public class MetastoreMessageHandler implements IMessageHandler {

  private static final Logger LOG = LoggerFactory.getLogger(MetastoreMessageHandler.class);

  public static final String RESOURCE_TYPE = "application/vnd.datamanager.metadata-record+json";

  private Handlebars hb;

  @Autowired
  private ApplicationProperties properties;

  @Autowired
  private MappingService mappingService;

  @Autowired
  private IndexingService elasticsearchService;

  @Autowired
  private IConsumerEngine consumer;

  public static final String RESOURCE_URL = "resolvingUrl";
  public static final String MAPPING_ID = "documentType";

  MetastoreMessageHandler(ApplicationProperties configuration, MappingService mappingService, IndexingService indexingService) {
    properties = configuration;
    this.mappingService = mappingService;
    this.elasticsearchService = indexingService;
  }

  @Override
  public RESULT handle(BasicMessage message) {
    LOG.debug("Successfully received message with routing key {}.", message.getRoutingKey());

    String resourceUrlAsString = message.getMetadata().get(RESOURCE_URL);
    String mappingId = message.getMetadata().get(MAPPING_ID);

    if ((resourceUrlAsString == null) || (mappingId == null)) {
      LOG.debug("Reject message: Missing properties!");
      return RESULT.REJECTED;
    }
    if (!MessageHandlerUtils.isAddressed(this.getHandlerIdentifier(), message)) {
      LOG.debug("Reject message: Not addressed correctly!");
      return RESULT.REJECTED;
    }

    int beginIndex = mappingId.lastIndexOf("/") + 1;
    int endIndex = mappingId.indexOf("?", beginIndex);
    if (endIndex < 0) {
      endIndex = mappingId.length();
    }
    String schemaId = mappingId.substring(beginIndex, endIndex);
    LOG.trace("Strip mappingId to id only...");
    mappingId = mappingId.substring(beginIndex);
    mappingId = mappingId.replace("=", "_");
    LOG.trace("New mappingId: '{}'", mappingId);
    LOG.trace("SchemaId: '{}'", schemaId);

    Path resultPath = null;
    try {
      LOG.debug("This message is for me: {}", message);

      URI resourceUri = new URI(resourceUrlAsString);
      // right now only one mapping is allowed.
      List<Path> pathWithAllMappings = mappingService.executeMapping(resourceUri, mappingId);
      if (pathWithAllMappings.isEmpty()) {
        return RESULT.FAILED;
      }
      resultPath = pathWithAllMappings.get(0);
      // Prefix index with configured value. (default: metastore-
      String index = properties.getElasticsearchIndex() + schemaId;
      String newIndex = ElasticsearchUtil.testForValidIndex(index);
      if (!index.equals(newIndex)) {
        LOG.warn("The index for elasticsearch '{}' was transformed to '{}' due to restrictions of elasticsearch!", index, newIndex);
        index = newIndex;
      }

      String jsonDocument = FileUtils.readFileToString(resultPath.toFile(), StandardCharsets.UTF_8);
      String resourceUrlAsStringWithoutVersion = removeFilterFromUri(resourceUrlAsString);
      elasticsearchService.uploadToElastic(jsonDocument, index, properties.getElasticsearchType(), resourceUrlAsStringWithoutVersion);

    } catch (URISyntaxException ex) {
      String errorMessage = String.format("Error downloading content from '%s': %s", resourceUrlAsString, ex.getMessage());
      LOG.error(errorMessage, ex);
      return RESULT.FAILED;
    } catch (IOException ioex) {
      String errorMessage = String.format("Error reading mapping file from '%s'", resultPath.toString());
      LOG.error(errorMessage, ioex);
      return RESULT.FAILED;
    } catch (IndexerException iex) {
      String errorMessage = String.format("Error while mapping content from '%s': %s", resourceUrlAsString, iex.getMessage());
      LOG.error(errorMessage, iex);
      return RESULT.FAILED;
    }
    return RESULT.SUCCEEDED;
  }

  @Override
  public boolean configure() {
    return true;
  }
    
  /**
   * Remove version and other stuff added to the URI.
   * @param uri URI of the object.
   * @return  URI without additional parameter.
   */
  public String removeFilterFromUri(String uri) {
    String strippedUri = null;
    if (uri != null) {
      strippedUri = uri.split("\\?", -1)[0];
    }
    return strippedUri;
  }

}
