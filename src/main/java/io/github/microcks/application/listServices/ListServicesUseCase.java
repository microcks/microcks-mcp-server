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

import io.github.microcks.domain.AgentErrorMessageHelper;
import io.github.microcks.domain.Result;
import io.github.microcks.domain.listServices.MicrocksAccessException;
import io.github.microcks.domain.listServices.ServiceSummary;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.List;


/**
 * Use case for listing Microcks services. Returns a Result with either the list of services or an AI-friendly error
 * message with retry/abandon guidance.
 * <p>
 * This use case depends only on the domain and uses a port (interface) to access infrastructure.
 * </p>
 */
@ApplicationScoped
public class ListServicesUseCase {

   @Inject
   MicrocksServicesPort microcksServicesPort;

   @Inject
   Logger logger;

   /**
    * List all available services from Microcks.
    *
    * @return Result containing either services or an AI-friendly error message
    */
   public Result<List<ServiceSummary>> listAllServices() {
      logger.debug("UseCase: listing all services");

      try {
         List<ServiceSummary> services = microcksServicesPort.listAllServices();
         logger.debugf("Successfully retrieved %d services", services.size());
         return Result.success(services);

      } catch (MicrocksAccessException e) {
         logger.errorf(e, "Microcks access failed: %s", e.getMessage());
         return Result.error(e.getAgentErrorMessage());

      } catch (Exception e) {
         logger.errorf(e, "Unexpected error listing services: %s", e.getMessage());
         return Result.error(AgentErrorMessageHelper.fromUnexpectedException(e));
      }
   }
}
