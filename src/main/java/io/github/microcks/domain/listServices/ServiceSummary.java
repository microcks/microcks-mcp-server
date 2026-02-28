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
package io.github.microcks.domain.listServices;


/**
 * Domain object representing a summary of a Microcks service. Contains only essential information for AI agents to
 * discover services.
 * <p>
 * Implemented as a Java record for immutability and automatic serialization support without framework-specific
 * annotations.
 * </p>
 */
public record ServiceSummary(String id, String name, String version, String type) {


   public String getId() {
      return id;
   }

   public String getName() {
      return name;
   }


   public String getVersion() {
      return version;
   }


   public String getType() {
      return type;
   }
}
