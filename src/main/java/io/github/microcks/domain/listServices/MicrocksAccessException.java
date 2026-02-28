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
 * Abstract base exception for Microcks access errors. Use specific subclasses:
 * <ul>
 * <li>{@link MicrocksUnauthorizedException} for HTTP 401 errors</li>
 * <li>{@link MicrocksForbiddenException} for HTTP 403 errors</li>
 * <li>{@link MicrocksUnknownAccessException} for unknown or other errors</li>
 * </ul>
 */
public abstract class MicrocksAccessException extends Exception {

   protected MicrocksAccessException(Throwable cause) {
      super(cause);
   }

   /**
    * Get error message formatted for AI agents with action guidance.
    *
    * @return AI-friendly error message with suggested action
    */
   public abstract String getAgentErrorMessage();
}
