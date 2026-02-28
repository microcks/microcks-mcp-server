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

import io.github.microcks.application.listServices.MicrocksServicesPort;
import io.github.microcks.client.ApiClient;
import io.github.microcks.client.ApiException;
import io.github.microcks.client.api.MockApi;
import io.github.microcks.client.model.Service;
import io.github.microcks.domain.listServices.MicrocksAccessException;
import io.github.microcks.domain.listServices.ServiceSummary;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Infrastructure adapter: HTTP client for Microcks API. Implements the MicrocksServicesPort interface for integration
 * with the Microcks REST API.
 */
@ApplicationScoped
public class MicrocksHttpAdapter implements MicrocksServicesPort {

   @ConfigProperty(name = "microcks.api.url")
   String microcksApiUrl;

   @Inject
   Logger logger;

   private MockApi mockApi;

   /**
    * Initialize the HTTP client lazily on first access.
    */
   private MockApi getMockApi() {
      if (mockApi == null) {
         logger.infof("Initializing Microcks HTTP client with URL: %s/api", microcksApiUrl);
         ApiClient apiClient = new ApiClient();
         apiClient.updateBaseUri(microcksApiUrl + "/api");
         mockApi = new MockApi(apiClient);
      }
      return mockApi;
   }

   /**
    * Retrieve all available services from Microcks.
    *
    * @return List of service summaries
    * @throws MicrocksAccessException for access errors (check isAuthenticationError() for 401/403)
    */
   public List<ServiceSummary> listAllServices() throws MicrocksAccessException {
      try {
         logger.debug("Fetching services from Microcks HTTP API");
         List<Service> services = getMockApi().getServices(null, null);

         return services.stream()
            .map(this::toServiceSummary)
            .collect(Collectors.toList());

      } catch (ApiException e) {
         throw MicrocksExceptionMapper.map(e);
      }
   }

   /**
    * Convert Microcks Service API object to domain ServiceSummary.
    */
   private ServiceSummary toServiceSummary(Service service) {
      return new ServiceSummary(service.getId(), service.getName(), service.getVersion(), service.getType()
         .toString());
   }
}
