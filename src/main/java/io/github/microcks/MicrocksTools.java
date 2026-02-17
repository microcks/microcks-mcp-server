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
package io.github.microcks;

import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MicrocksTools {

   @Tool(name = "microcks_import_artifact", description = "Import a file artifact (OpenAPI, AsyncAPI, "
         + "GraphQL schema, Protobuffer file or Collection) into Microcks")
   ToolResponse importArtifact(@ToolArg(description = "Artifact file name") String name) {
      return ToolResponse.success("Imported " + name);
   }
}
