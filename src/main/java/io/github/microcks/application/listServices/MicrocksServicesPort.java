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

import io.github.microcks.domain.listServices.MicrocksAccessException;
import io.github.microcks.domain.listServices.ServiceSummary;

import java.util.List;


/**
 * Port (interface) for accessing Microcks services. This is a secondary/driven port in hexagonal architecture -
 * implemented by infrastructure adapters.
 */
public interface MicrocksServicesPort {

   /**
    * Retrieve all available services from Microcks.
    *
    * @return List of service summaries
    * @throws MicrocksAccessException if unable to access Microcks
    */
   List<ServiceSummary> listAllServices() throws MicrocksAccessException;
}
