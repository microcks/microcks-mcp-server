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
package io.github.microcks.domain;


/**
 * Helper to build AI agent-friendly error messages for exceptions.
 */
public final class AgentErrorMessageHelper {

   private AgentErrorMessageHelper() {
      // Utility class
   }

   /**
    * Build an error message for unexpected exceptions. These are likely bugs - retrying won't help.
    *
    * @param e the unexpected exception
    * @return AI-friendly error message with guidance
    */
   public static String fromUnexpectedException(Exception e) {
      StringBuilder message = new StringBuilder();
      message.append("Unexpected internal error: ");
      message.append(". Do not retry. This is likely a bug - report this issue.");
      return message.toString();
   }
}
