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
 * Exception thrown when access to Microcks is forbidden (HTTP 403). Credentials are valid but insufficient permissions.
 */
public class MicrocksForbiddenException extends MicrocksAccessException {

   public MicrocksForbiddenException(Throwable cause) {
      super(cause);
   }

   @Override
   public String getAgentErrorMessage() {
      return "Microcks access denied. " + "The credentials are valid but lack required permissions. "
            + "Do not retry. Ask the user to request access from the Microcks administrator.";
   }
}
