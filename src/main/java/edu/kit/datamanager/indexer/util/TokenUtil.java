/*
 * Copyright 2020 KIT
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
package edu.kit.datamanager.indexer.util;

import edu.kit.datamanager.entities.RepoServiceRole;
import edu.kit.datamanager.util.JwtBuilder;
import java.util.Calendar;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class TokenUtil {

  /**
   * Logger for this class.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(TokenUtil.class);
  private String jwtSecret;
  private JwtBuilder serviceToken;

  public TokenUtil(String jwtSecret) {
    this.jwtSecret = jwtSecret;
    serviceToken = JwtBuilder.createServiceToken("indexer_service", RepoServiceRole.SERVICE_READ);

  }

  /**
   * Creates a compact token with the secret set in beforehand. Token is valid
   * for 30 seconds!
   *
   * @return service token with a validity of 30 seconds.
   *
   * @param resourceURL the given URI
   * @return the path to the created file.
   */
  public String getCompactToken(int noOfSeconds) {
    String compactToken = null;
    if (jwtSecret != null) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(new Date());
      noOfSeconds = noOfSeconds <= 0 ? 10 : noOfSeconds;
      LOGGER.trace("Create a token that is valid for '{}' seconds!", noOfSeconds);
      calendar.add(Calendar.SECOND, noOfSeconds);
      compactToken = serviceToken.getCompactToken(jwtSecret, calendar.getTime());
    }
    return compactToken;
  }

}
