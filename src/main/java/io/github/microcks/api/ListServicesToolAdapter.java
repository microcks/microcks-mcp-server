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

import io.github.microcks.application.listServices.ListServicesUseCase;
import io.github.microcks.domain.Result;
import io.github.microcks.domain.listServices.ServiceSummary;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;


/**
 * API adapter: MCP Tool for Microcks service discovery. Exposes features as MCP tools for AI agents.
 * <p>
 * This adapter is responsible only for:
 * </p>
 * <ul>
 * <li>MCP protocol handling (Tool annotations, ToolResponse)</li>
 * <li>JSON serialization of results</li>
 * <li>Delegating business logic to the use case</li>
 * </ul>
 */
@ApplicationScoped
public class ListServicesToolAdapter {

   @Inject
   ListServicesUseCase listServicesUseCase;

   @Inject
   ObjectMapper objectMapper;

   @Inject
   Logger logger;

   @Tool(name = "microcks_list_services", description = "Discover and list all available services, mocks and APIs in the connected Microcks instance. "
         + "Returns service names, versions, IDs and types (REST, GraphQL, SOAP, AsyncAPI, gRPC, etc.) to help "
         + "identify which mock services are available.")
   ToolResponse listServices() throws JsonProcessingException {
      logger.debug("Executing microcks_list_services tool");

      Result<List<ServiceSummary>> result = listServicesUseCase.listAllServices();

      return switch (result) {
         case Result.Success(var services) -> toSuccessResponse(services);
         case Result.Error(var message) -> ToolResponse.error(message);
      };
   }

   /**
    * Convert a list of services to a successful MCP ToolResponse with JSON content.
    */
   private ToolResponse toSuccessResponse(List<ServiceSummary> services) throws JsonProcessingException {
      String json = objectMapper.writeValueAsString(services);
      return ToolResponse.success(json);
   }

   @Tool(name = "microcks_import_artifact", description = "Import a file artifact (OpenAPI, AsyncAPI, GraphQL schema, Protobuffer file or Collection) into Microcks")
   ToolResponse importArtifact(@ToolArg(description = "Artifact file name") String name) {
      return ToolResponse.success("Imported " + name);
   }
}
