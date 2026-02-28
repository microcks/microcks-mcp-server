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
 * Exception thrown for unknown Microcks access errors. Used when the HTTP status code is neither 401 nor 403, or when
 * the error type cannot be determined.
 */
public class MicrocksUnknownAccessException extends MicrocksAccessException {

   public MicrocksUnknownAccessException(Throwable cause) {
      super(cause);
   }

   @Override
   public String getAgentErrorMessage() {
      return "Failed to connect to Microcks server. " + "This may be a network issue or the server may be down. "
            + "You may retry once. If it still fails, ask the user to check the server status.";
   }
}
