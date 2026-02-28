/*
 * Copyright The Microcks Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.microcks.api;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkiverse.mcp.server.test.McpAssured;
import io.quarkiverse.mcp.server.test.McpAssured.McpStreamableTestClient;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Integration test for microcks_list_services MCP tool using McpAssured. Tests the tool via the HTTP streamable MCP
 * endpoint.
 */
@QuarkusTest
class ListServicesToolTest {

   /**
    * Tests that the microcks_list_services tool can be invoked via the MCP HTTP streamable endpoint.
    * <p>
    * Validates that: - The tool is accessible through McpAssured streamable client - The response is not null even when
    * Microcks instance is unavailable - The response content structure is valid (may contain success or error)
    * </p>
    */
   @Test
   void shouldListServicesViaStreamableHttpEndpoint() {
      // Arrange
      McpStreamableTestClient client = McpAssured.newConnectedStreamableClient();

      // Act & Assert
      client.when()
         .toolsCall("microcks_list_services", Map.of(), response -> {
            // The tool should be callable even if Microcks is not running
            // In that case, it returns an error response
            assertNotNull(response, "Response should not be null");
            assertNotNull(response.content(), "Response content should not be null");
         })
         .thenAssertResults();

      client.disconnect();
   }

   /**
    * Tests that microcks_list_services tool is properly registered in the MCP tools list.
    * <p>
    * Validates that: - The MCP server exposes at least one tool - The microcks_list_services tool is discoverable by
    * name - The tool has a description containing "list" keyword
    * </p>
    */
   @Test
   void shouldListToolsIncludingMicrocksListServices() {
      // Arrange
      McpStreamableTestClient client = McpAssured.newConnectedStreamableClient();

      // Act & Assert
      client.when()
         .toolsList(page -> {
            assertNotNull(page, "Tool page should not be null");
            assertTrue(page.size() > 0, "Should have at least one tool");

            var tool = page.findByName("microcks_list_services");
            assertNotNull(tool, "microcks_list_services tool should be registered");
            assertNotNull(tool.description(), "Tool should have a description");
            assertTrue(tool.description()
               .contains("list"), "Description should mention listing");
         })
         .thenAssertResults();

      client.disconnect();
   }

   /**
    * Tests that microcks_import_artifact tool is properly registered in the MCP tools list.
    * <p>
    * Validates that: - The microcks_import_artifact tool is discoverable by name - The tool has a description
    * containing "Import" keyword
    * </p>
    */
   @Test
   void shouldListMicrocksImportArtifactTool() {
      // Arrange
      McpStreamableTestClient client = McpAssured.newConnectedStreamableClient();

      // Act & Assert
      client.when()
         .toolsList(page -> {
            var tool = page.findByName("microcks_import_artifact");
            assertNotNull(tool, "microcks_import_artifact tool should be registered");
            assertTrue(tool.description()
               .contains("Import"), "Description should mention importing");
         })
         .thenAssertResults();

      client.disconnect();
   }
}
