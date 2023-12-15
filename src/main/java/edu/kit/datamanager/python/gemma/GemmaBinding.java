/*
* Copyright 2019 Karlsruhe Institute of Technology.
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
package edu.kit.datamanager.python.gemma;

import edu.kit.datamanager.clients.SimpleServiceClient;
import edu.kit.datamanager.indexer.configuration.ApplicationProperties;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

/**
 *
 * @author jejkal
 */
@Component
public class GemmaBinding {

  private static final Logger LOGGER = LoggerFactory.getLogger(GemmaBinding.class);

  @Autowired
  ApplicationProperties gemmaConfiguration;

  /**
   * Downloads the file behind the given URI and returns its content as a
   * string.
   *
   * @param resourceURL the given URI
   * @return the content of the file (the body of the response) as a string.
   * null if a problem occurred.
   */
  public Optional<String> downloadResource(URI resourceURL) {
    String content = null;
    try {
      content = SimpleServiceClient
              .create(resourceURL.toString())
              .accept(MediaType.TEXT_PLAIN)
              .getResource(String.class);
    } catch (Throwable tw) {
      LOGGER.error("Error reading URI '" + resourceURL.toString() + "'", tw);
    }
    return Optional.ofNullable(content);
  }

  /**
   * Stores the given content in a file with the given name within the systems
   * temporary directory.
   *
   * @param content the given content
   * @param filename the given filename
   * @return the absolute path to the stored file on success.
   */
  public Optional<Path> storeAsTempFile(String content, String filename) {
    if (content == null || filename == null) {
      LOGGER.error("Did not receive any resource in the response body. Unable to continue.");
      return Optional.empty();
    }

    File directory = Paths.get(System.getProperty("java.io.tmpdir")).toFile();
    File target = new File(directory, filename);
    Path targetPath = Paths.get(target.getAbsolutePath());
    try (FileOutputStream out = new FileOutputStream(target)) {
      LOGGER.trace("Writing data resource to temporary file {}.", targetPath);
      out.write(content.getBytes());
    } catch (Exception e) {
      LOGGER.error("Failed to write data resource to temporary file.", e);
    }
    return Optional.ofNullable(targetPath);
  }

  /**
   * Not implemented yet.
   *
   * @param filepath Path to file.
   * @param schema Path to schema.
   * @return Path to mapped file.
   */
  public Path mapSingleFile(Path filepath, Path schema) {
    // TODO
    return Paths.get("tmp");
  }

}
