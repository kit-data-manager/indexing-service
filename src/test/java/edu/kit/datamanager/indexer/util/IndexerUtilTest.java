/*
 * Copyright 2020 hartmann-v.
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

import edu.kit.datamanager.python.gemma.GemmaConfiguration;
import edu.kit.datamanager.python.gemma.GemmaMapping;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Optional;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hartmann-v
 */
public class IndexerUtilTest {
  
  public IndexerUtilTest() {
  }
  
  @BeforeClass
  public static void setUpClass() {
  }
  
  @AfterClass
  public static void tearDownClass() {
  }
  
  @Before
  public void setUp() {
  }
  
  @After
  public void tearDown() {
  }

 
  /**
   * Test of downloadResource method, of class GemmaMapping.
   */
  @Test
  public void testDownloadResource() throws URISyntaxException {
    System.out.println("downloadResource");
    assertNotNull(new IndexerUtil());
    URI resourceURL = new URI("https://www.example.org");
     Optional<Path> result = IndexerUtil.downloadResource(resourceURL);
    assertTrue(result.isPresent());
    assertTrue(result.get().toFile().exists());
    assertTrue(result.get().toFile().delete());

    resourceURL = new URI("https://invalidhttpaddress.de");
    result = IndexerUtil.downloadResource(resourceURL);
    assertTrue(!result.isPresent());
  }
  
}
