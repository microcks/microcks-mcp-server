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
package io.github.microcks.api;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.test.junit.QuarkusTest;


/**
 * Base test class providing common configuration injection for Microcks integration tests.
 */
@QuarkusTest
public class BaseTest {

   @ConfigProperty(name = "quarkus.http.test-port")
   protected int quarkusHttpPort;

   @ConfigProperty(name = "quarkus.microcks.default.http")
   protected String microcksContainerUrl;
}
