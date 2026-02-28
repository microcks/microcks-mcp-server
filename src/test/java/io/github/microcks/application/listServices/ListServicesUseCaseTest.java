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
package io.github.microcks.application.listServices;

import io.github.microcks.domain.Result;
import io.github.microcks.domain.listServices.MicrocksForbiddenException;
import io.github.microcks.domain.listServices.MicrocksUnauthorizedException;
import io.github.microcks.domain.listServices.MicrocksUnknownAccessException;
import io.github.microcks.domain.listServices.ServiceSummary;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * London-style (mockist) unit tests for ListServicesUseCase. Tests the use case in isolation by mocking dependencies.
 */
@QuarkusTest
class ListServicesUseCaseTest {

   @Inject
   ListServicesUseCase useCase;

   @InjectMock
   MicrocksServicesPort microcksServicesPort;

   // --- Success cases ---

   @Test
   void shouldReturnSuccessWithServicesWhenAdapterReturnsMultipleServices() throws Exception {
      // Arrange
      ServiceSummary service1 = new ServiceSummary("id1", "Petstore API", "1.0.0", "REST");
      ServiceSummary service2 = new ServiceSummary("id2", "Weather API", "2.1.0", "GRAPHQL");
      ServiceSummary service3 = new ServiceSummary("id3", "User Service", "3.0.0", "GRPC");
      List<ServiceSummary> expectedServices = List.of(service1, service2, service3);
      when(microcksServicesPort.listAllServices()).thenReturn(expectedServices);

      // Act
      Result<List<ServiceSummary>> result = useCase.listAllServices();

      // Assert with pattern matching
      switch (result) {
         case Result.Success(var services) -> {
            assertEquals(expectedServices, services, "Use case should return the same list as the adapter");
            assertEquals(3, services.size(), "Should return 3 services");
         }
         case Result.Error(var message) -> fail("Expected success but got error: " + message);
      }
      verify(microcksServicesPort, times(1)).listAllServices();
   }

   @Test
   void shouldReturnSuccessWithEmptyListWhenAdapterReturnsEmpty() throws Exception {
      // Arrange
      List<ServiceSummary> emptyList = Collections.emptyList();
      when(microcksServicesPort.listAllServices()).thenReturn(emptyList);

      // Act
      Result<List<ServiceSummary>> result = useCase.listAllServices();

      // Assert with pattern matching
      switch (result) {
         case Result.Success(var services) ->
            assertTrue(services.isEmpty(), "Use case should return empty list when adapter returns empty");
         case Result.Error(var message) -> fail("Expected success but got error: " + message);
      }
      verify(microcksServicesPort, times(1)).listAllServices();
   }

   @Test
   void shouldReturnSuccessWithSingleServiceWhenAdapterReturnsOne() throws Exception {
      // Arrange
      ServiceSummary singleService = new ServiceSummary("single-id", "Solo API", "42.0.0", "SOAP");
      List<ServiceSummary> singletonList = List.of(singleService);
      when(microcksServicesPort.listAllServices()).thenReturn(singletonList);

      // Act
      Result<List<ServiceSummary>> result = useCase.listAllServices();

      // Assert with pattern matching
      switch (result) {
         case Result.Success(var services) -> {
            assertEquals(1, services.size(), "Should return exactly one service");
            assertSame(singleService, services.get(0), "Should return the exact service instance");
         }
         case Result.Error(var message) -> fail("Expected success but got error: " + message);
      }
      verify(microcksServicesPort, times(1)).listAllServices();
   }

   // --- Error cases ---

   @Test
   void shouldReturnErrorWithNoRetryGuidanceWhenUnauthorized() throws Exception {
      // Arrange
      when(microcksServicesPort.listAllServices())
            .thenThrow(new MicrocksUnauthorizedException(new RuntimeException("Authentication required")));

      // Act
      Result<List<ServiceSummary>> result = useCase.listAllServices();

      // Assert with pattern matching
      switch (result) {
         case Result.Success(var _) -> fail("Expected error but got success");
         case Result.Error(var message) -> {
            assertTrue(message.contains("authentication"), "Should mention authentication issue");
            assertTrue(message.contains("Do not retry"), "Should advise against retry");
         }
      }
      verify(microcksServicesPort, times(1)).listAllServices();
   }

   @Test
   void shouldReturnErrorWithNoRetryGuidanceWhenForbidden() throws Exception {
      // Arrange
      when(microcksServicesPort.listAllServices())
            .thenThrow(new MicrocksForbiddenException(new RuntimeException("Access forbidden")));

      // Act
      Result<List<ServiceSummary>> result = useCase.listAllServices();

      // Assert with pattern matching
      switch (result) {
         case Result.Success(var _) -> fail("Expected error but got success");
         case Result.Error(var message) -> {
            assertTrue(message.contains("denied"), "Should mention access denied");
            assertTrue(message.contains("Do not retry"), "Should advise against retry");
         }
      }
      verify(microcksServicesPort, times(1)).listAllServices();
   }

   @Test
   void shouldReturnErrorForGenericMicrocksAccessException() throws Exception {
      // Arrange
      when(microcksServicesPort.listAllServices())
            .thenThrow(new MicrocksUnknownAccessException(new RuntimeException("Connection refused")));

      // Act
      Result<List<ServiceSummary>> result = useCase.listAllServices();

      // Assert with pattern matching
      switch (result) {
         case Result.Success(var _) -> fail("Expected error but got success");
         case Result.Error(var message) -> assertTrue(message.contains("retry"), "Should mention retry possibility");
      }
      verify(microcksServicesPort, times(1)).listAllServices();
   }

   @Test
   void shouldReturnErrorForUnexpectedRuntimeException() throws Exception {
      // Arrange
      when(microcksServicesPort.listAllServices()).thenThrow(new RuntimeException("Unexpected NullPointer"));

      // Act
      Result<List<ServiceSummary>> result = useCase.listAllServices();

      // Assert with pattern matching
      switch (result) {
         case Result.Success(var _) -> fail("Expected error but got success");
         case Result.Error(var message) -> {
            assertTrue(message.contains("Unexpected"), "Should mention unexpected error");
            assertTrue(message.contains("Do not retry"), "Should advise against retry for bugs");
         }
      }
      verify(microcksServicesPort, times(1)).listAllServices();
   }

   @Test
   void shouldDelegateToAdapterExactlyOnce() throws Exception {
      // Arrange
      List<ServiceSummary> services = List.of(new ServiceSummary("id1", "Test API", "1.0", "REST"));
      when(microcksServicesPort.listAllServices()).thenReturn(services);

      // Act
      useCase.listAllServices();

      // Assert - Verify delegation happens exactly once
      verify(microcksServicesPort, times(1)).listAllServices();
      verifyNoMoreInteractions(microcksServicesPort);
   }
}
