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
package edu.kit.datamanager.indexer.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.kit.datamanager.clients.SimpleServiceClient;
import edu.kit.datamanager.exceptions.BadArgumentException;
import edu.kit.datamanager.indexer.configuration.ApplicationProperties;
import edu.kit.datamanager.indexer.dao.IMappingRecordDao;
import edu.kit.datamanager.indexer.domain.AclRecord;
import edu.kit.datamanager.indexer.domain.MappingRecord;
import edu.kit.datamanager.indexer.exception.IndexerException;
import edu.kit.datamanager.indexer.mapping.Mapping;
import edu.kit.datamanager.indexer.mapping.MappingUtil;
import edu.kit.datamanager.indexer.util.IndexerUtil;
import edu.kit.datamanager.indexer.util.TokenUtil;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for managing mappings.
 */
@Service
public class MappingService {

  /**
   * Repo holding all MappingRecords.
   */
  @Autowired
  private IMappingRecordDao mappingRepo;
  /**
   * Path to directory holding all mapping files.
   */
  private Path mappingsDirectory;

  /**
   * MappingUtil for executing mappings.
   */
  private MappingUtil mappingUtil;

  /**
   * Utility for bearer tokens to enable authorized access.
   */
  private TokenUtil tokenUtil;
  /**
   * Logger for this class.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(MappingService.class);

  private static final String MAPPING_ERROR_MESSAGE = "Error: Mapping '";

  @Autowired
  public MappingService(ApplicationProperties applicationProperties) throws URISyntaxException {
    init(applicationProperties);
  }

  /**
   * Save content to mapping file and get the mapping location.
   *
   * @param content Content of the mapping file.
   * @param mappingRecord record of the mapping
   * @throws java.io.IOException Error writing file
   */
  public void createMapping(String content, MappingRecord mappingRecord) throws IOException {
    // Right now only one mapping per mappingID is allowed. May change in future.
    Iterable<MappingRecord> findMapping = mappingRepo.findByMappingIdInOrMappingTypeIn(Arrays.asList(mappingRecord.getMappingId()), Arrays.asList((String) null));
    if (findMapping.iterator().hasNext()) {
      mappingRecord = findMapping.iterator().next();
      throw new IndexerException(MAPPING_ERROR_MESSAGE + mappingRecord.getMappingId() + "/" + mappingRecord.getMappingType() + "' already exists!");
    }
    saveMappingFile(content, mappingRecord);
    mappingRepo.save(mappingRecord);
  }

  /**
   * Update content of mapping file and get the mapping location.
   *
   * @param content Content of the mapping file.
   * @param mappingRecord record of the mapping.
   * @throws java.io.IOException Error reading/writing file.
   */
  public void updateMapping(String content, MappingRecord mappingRecord) throws IOException {
    Optional<MappingRecord> findMapping = mappingRepo.findByMappingIdAndMappingType(mappingRecord.getMappingId(), mappingRecord.getMappingType());
    if (!findMapping.isPresent()) {
      throw new IndexerException(MAPPING_ERROR_MESSAGE + mappingRecord.getMappingId() + "/" + mappingRecord.getMappingType() + "' doesn't exist!");
    }
    mappingRecord.setMappingDocumentUri(findMapping.get().getMappingDocumentUri());
    saveMappingFile(content, mappingRecord);
    mappingRepo.save(mappingRecord);
  }

  /**
   * Delete mapping file and its record.
   *
   * @param mappingRecord record of the mapping
   * @throws java.io.IOException Error reading file.
   */
  public void deleteMapping(MappingRecord mappingRecord) throws IOException {
    Optional<MappingRecord> findMapping = mappingRepo.findByMappingIdAndMappingType(mappingRecord.getMappingId(), mappingRecord.getMappingType());
    if (!findMapping.isPresent()) {
      throw new IndexerException(MAPPING_ERROR_MESSAGE + mappingRecord.getMappingId() + "/" + mappingRecord.getMappingType() + "' doesn't exist!");
    }
    mappingRecord = findMapping.get();
    deleteMappingFile(mappingRecord);
    mappingRepo.delete(mappingRecord);
  }

  /**
   * Execute mapping and get the location of result file. If no according
   * mapping is found the src file will be returned.
   *
   * @param contentUrl Content of the src file.
   * @param mappingId filename of the mapping
   * @param mappingType type of the mapping.
   *
   * @return Path to result file.
   */
  public Optional<Path> executeMapping(URI contentUrl, String mappingId, String mappingType) {
    Optional<Path> returnValue;
    Optional<Path> download = IndexerUtil.downloadResource(contentUrl, tokenUtil);
    MappingRecord mappingRecord;

    if (download.isPresent()) {
      LOGGER.trace("Execute Mapping for '{}', and mapping '{}/{}'.", contentUrl.toString(), mappingId, mappingType);
      Path srcFile = download.get();
      // Get mapping file
      Optional<MappingRecord> optionalMappingRecord = mappingRepo.findByMappingIdAndMappingType(mappingId, mappingType);
      LOGGER.trace("Mapping Record available: '{}'", optionalMappingRecord.isPresent());
      if (optionalMappingRecord.isPresent()) {
        mappingRecord = optionalMappingRecord.get();
        mappingRecord.getMappingDocumentUri();
        Path mappingFile = Paths.get(mappingRecord.getMappingDocumentUri());
        // execute mapping
        returnValue = mappingUtil.mapFile(mappingFile, srcFile, mappingType);
        // remove downloaded file
        IndexerUtil.removeFile(srcFile);
      } else {
        returnValue = Optional.of(srcFile);
      }
    } else {
      String message = contentUrl != null ? "Error: Downloading content from '" + contentUrl.toString() + "'!" : "Error: No URL provided!";
      throw new IndexerException(message);
    }
    return returnValue;
  }

  /**
   * Execute mapping(s) and get the location of result file.
   *
   * @param contentUrl Content of the src file.
   * @param mappingId filename of the mapping
   *
   * @return List of paths to all result files.
   */
  public List<Path> executeMapping(URI contentUrl, String mappingId) {
    List<Path> returnValue = new ArrayList<>();
    String noMappingType = null;

    Iterator<MappingRecord> findMapping = mappingRepo.findByMappingIdInOrMappingTypeIn(Arrays.asList(mappingId), Arrays.asList(noMappingType)).iterator();
    String mappingType = null;
    if (findMapping.hasNext()) {
      // If there are several mappings right now only the first mapping is selected. 
      mappingType = findMapping.next().getMappingType();
    }
    LOGGER.trace("Found mappingType: '{}'", mappingType);
    Optional<Path> executeMapping = executeMapping(contentUrl, mappingId, mappingType);
    if (executeMapping.isPresent()) {
      returnValue.add(executeMapping.get());
      Path metadataDocumentPath = executeMapping.get();
      ObjectMapper mapper = new ObjectMapper();
      JsonNode rootNode = null;
      String jsonString;
      try {
        jsonString = FileUtils.readFileToString(metadataDocumentPath.toFile(), StandardCharsets.UTF_8);
        rootNode = mapper.readValue(jsonString, JsonNode.class);
      } catch (IOException ex) {
        LOGGER.error("Error parsing metadata document", ex);
        throw new BadArgumentException(ex.getMessage());
      }
      // Try to read ACL from contentURI
      try {
        AclRecord aclRecord;

        aclRecord = getAclRecord(contentUrl, tokenUtil);
        aclRecord.setMetadataDocument(rootNode);
        mapper.writeValue(executeMapping.get().toFile(), aclRecord);
      } catch (IOException ex) {
        LOGGER.error("Error while reading mapped file!?", ex);
      } catch (Throwable tw) {
        LOGGER.warn("Error reading ACL from URI '" + contentUrl.toString() + "'", tw);
      }
    }
    return returnValue;
  }

  private AclRecord getAclRecord(URI contentUrl, TokenUtil tokenUtil) {
    AclRecord aclRecord = null;
    if (contentUrl != null) {
      String compactToken = tokenUtil == null ? null : tokenUtil.getCompactToken(30);
      if (contentUrl.getHost() != null) {
        SimpleServiceClient ssc = SimpleServiceClient.create(contentUrl.toString());
        ssc.accept(AclRecord.ACL_RECORD_MEDIA_TYPE);
        if (compactToken != null) {
          ssc.withBearerToken(compactToken);
        }
        AclRecord aclRecordFromServer = ssc.getResource(AclRecord.class);
        if (aclRecordFromServer != null) {
          aclRecord = aclRecordFromServer;
        }
      }
    }
    return aclRecord;
  }

  /**
   * Initalize mappings directory and mappingUtil instance.
   *
   * @param applicationProperties Properties holding mapping directory setting.
   */
  private void init(ApplicationProperties applicationProperties) throws URISyntaxException {
    if ((applicationProperties != null) && (applicationProperties.getMappingsLocation() != null)) {
      mappingUtil = new MappingUtil(applicationProperties);
      try {
        mappingsDirectory = Files.createDirectories(new File(applicationProperties.getMappingsLocation().getPath()).getAbsoluteFile().toPath());
      } catch (IOException e) {
        throw new IndexerException("Could not initialize directory '" + applicationProperties.getMappingsLocation() + "' for mapping.", e);
      }
    } else {
      throw new IndexerException("Could not initialize mapping directory due to missing location!");
    }
    tokenUtil = new TokenUtil(applicationProperties.getJwtSecret());
  }

  /**
   * Save mapping file to mapping directory.
   *
   * @param content Content of file.
   * @param mapping record of mapping
   * @throws IOException error writing file.
   */
  private void saveMappingFile(String content, MappingRecord mapping) throws IOException {
    Path newMappingFile;
    if ((content != null) && (mapping != null) && (mapping.getMappingId() != null) && (mapping.getMappingType() != null)) {
      LOGGER.debug("Storing mapping file with id '{}' and type '{}'", mapping.getMappingId(), mapping.getMappingType());
      LOGGER.trace("Content of mapping: '{}'", content);
      try {
        Mapping.valueOf(mapping.getMappingType());
        // 'delete' old file
        deleteMappingFile(mapping);
        newMappingFile = Paths.get(mappingsDirectory.toString(), mapping.getMappingId() + "_" + mapping.getMappingType() + ".mapping");
        LOGGER.trace("Write content to '{}'", newMappingFile.toString());
        FileUtils.writeStringToFile(newMappingFile.toFile(), content, StandardCharsets.UTF_8);
        mapping.setMappingDocumentUri(newMappingFile.toString());
        byte[] data = content.getBytes();

        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.update(data, 0, data.length);

        mapping.setDocumentHash("sha1:" + Hex.encodeHexString(md.digest()));
      } catch (NoSuchAlgorithmException ex) {
        String message = "Failed to initialize SHA1 MessageDigest.";
        LOGGER.error(message, ex);
        throw new IndexerException(message, ex);
      } catch (IllegalArgumentException iae) {
        String message = "Error: Unkown mapping! (" + mapping.getMappingType() + ")";
        LOGGER.error(message, iae);
        throw new IndexerException(message, iae);
      }
    } else {
      throw new IndexerException("Error saving mapping file! (no content)");
    }
  }

  /**
   * Delete mapping file. The file will not be deleted. Add actual datetime as
   * suffix
   *
   * @param mapping record of mapping
   * @throws IOException error writing file.
   */
  private void deleteMappingFile(MappingRecord mapping) throws IOException {
    if ((mapping != null) && (mapping.getMappingDocumentUri() != null)) {
      LOGGER.debug("Delete mapping file '{}'", mapping.getMappingDocumentUri());
      Path deleteFile = Paths.get(mapping.getMappingDocumentUri());
      if (deleteFile.toFile().exists()) {
        Path newFileName = Paths.get(deleteFile.getParent().toString(), deleteFile.getFileName() + date2String());
        LOGGER.trace("Move mapping file fo '{}'", newFileName.toString());
        FileUtils.moveFile(deleteFile.toFile(), newFileName.toFile());
      }
    }
  }

  /**
   * Create string from actual date.
   *
   * @return formatted date.
   */
  private String date2String() {
    SimpleDateFormat sdf = new SimpleDateFormat("_yyyyMMdd_HHmmss");
    return sdf.format(new Date());
  }
}
