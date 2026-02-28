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
package io.github.microcks.infrastructure;

import io.github.microcks.client.ApiException;
import io.github.microcks.domain.listServices.MicrocksAccessException;
import io.github.microcks.domain.listServices.MicrocksForbiddenException;
import io.github.microcks.domain.listServices.MicrocksUnauthorizedException;
import io.github.microcks.domain.listServices.MicrocksUnknownAccessException;


/**
 * Maps Microcks client exceptions to domain exceptions.
 */
public final class MicrocksExceptionMapper {

   private MicrocksExceptionMapper() {
      // Utility class
   }

   /**
    * Map an ApiException to a domain-specific exception based on HTTP status code.
    *
    * @param e the API exception to map
    * @return the corresponding domain exception
    */
   public static MicrocksAccessException map(ApiException e) {
      int statusCode = e.getCode();

      if (statusCode == 401) {
         return new MicrocksUnauthorizedException(e);
      }

      if (statusCode == 403) {
         return new MicrocksForbiddenException(e);
      }

      return new MicrocksUnknownAccessException(e);
   }

   /**
    * Map a generic exception to a domain exception.
    *
    * @param e the exception to map
    * @return the corresponding domain exception
    */
   public static MicrocksAccessException map(Exception e) {
      return new MicrocksUnknownAccessException(e);
   }
}
