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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Pure unit tests for MicrocksExceptionMapper utility class. No Quarkus context needed - fast execution.
 */
class MicrocksExceptionMapperTest {

   @Test
   void shouldMapApiExceptionWith401ToUnauthorizedException() {
      // Arrange
      ApiException apiException = new ApiException(401, "Unauthorized");

      // Act
      MicrocksAccessException result = MicrocksExceptionMapper.map(apiException);

      // Assert
      assertInstanceOf(MicrocksUnauthorizedException.class, result);
      assertNotNull(result.getAgentErrorMessage());
      assertTrue(result.getAgentErrorMessage()
         .contains("authentication"));
   }

   @Test
   void shouldMapApiExceptionWith403ToForbiddenException() {
      // Arrange
      ApiException apiException = new ApiException(403, "Forbidden");

      // Act
      MicrocksAccessException result = MicrocksExceptionMapper.map(apiException);

      // Assert
      assertInstanceOf(MicrocksForbiddenException.class, result);
      assertNotNull(result.getAgentErrorMessage());
   }

   @Test
   void shouldMapApiExceptionWith500ToUnknownAccessException() {
      // Arrange
      ApiException apiException = new ApiException(500, "Internal Server Error");

      // Act
      MicrocksAccessException result = MicrocksExceptionMapper.map(apiException);

      // Assert
      assertInstanceOf(MicrocksUnknownAccessException.class, result);
      assertNotNull(result.getAgentErrorMessage());
   }

   @Test
   void shouldMapApiExceptionWith404ToUnknownAccessException() {
      // Arrange
      ApiException apiException = new ApiException(404, "Not Found");

      // Act
      MicrocksAccessException result = MicrocksExceptionMapper.map(apiException);

      // Assert
      assertInstanceOf(MicrocksUnknownAccessException.class, result);
   }

   @Test
   void shouldMapGenericExceptionToUnknownAccessException() {
      // Arrange
      Exception genericException = new RuntimeException("Connection refused");

      // Act
      MicrocksAccessException result = MicrocksExceptionMapper.map(genericException);

      // Assert
      assertInstanceOf(MicrocksUnknownAccessException.class, result);
      assertEquals(genericException, result.getCause());
      assertNotNull(result.getAgentErrorMessage());
   }

   @Test
   void shouldMapNullPointerExceptionToUnknownAccessException() {
      // Arrange
      Exception exception = new NullPointerException("Null value encountered");

      // Act
      MicrocksAccessException result = MicrocksExceptionMapper.map(exception);

      // Assert
      assertInstanceOf(MicrocksUnknownAccessException.class, result);
   }
}
