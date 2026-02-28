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
package io.github.microcks.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;


/**
 * ArchUnit tests to enforce hexagonal architecture layer dependencies.
 * <p>
 * Layer dependency rules (outer to inner):
 * </p>
 * <ul>
 * <li>api → application → domain</li>
 * <li>infrastructure → application → domain</li>
 * </ul>
 * <p>
 * Restrictions:
 * </p>
 * <ul>
 * <li>domain must not depend on application, infrastructure, or api</li>
 * <li>application must not depend on infrastructure or api</li>
 * <li>infrastructure must not depend on api</li>
 * </ul>
 */
class HexagonalArchitectureTest {

   private static final String BASE_PACKAGE = "io.github.microcks";
   private static JavaClasses classes;

   @BeforeAll
   static void importClasses() {
      classes = new ClassFileImporter().withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
         .importPackages(BASE_PACKAGE);
   }

   @Test
   @DisplayName("Domain layer should not depend on application layer")
   void domainShouldNotDependOnApplication() {
      ArchRule rule = noClasses().that()
         .resideInAPackage("..domain..")
         .should()
         .dependOnClassesThat()
         .resideInAPackage("..application..")
         .allowEmptyShould(true);

      rule.check(classes);
   }

   @Test
   @DisplayName("Domain layer should not depend on infrastructure layer")
   void domainShouldNotDependOnInfrastructure() {
      ArchRule rule = noClasses().that()
         .resideInAPackage("..domain..")
         .should()
         .dependOnClassesThat()
         .resideInAPackage("..infrastructure..")
         .allowEmptyShould(true);

      rule.check(classes);
   }

   @Test
   @DisplayName("Domain layer should not depend on API layer")
   void domainShouldNotDependOnApi() {
      ArchRule rule = noClasses().that()
         .resideInAPackage("..domain..")
         .should()
         .dependOnClassesThat()
         .resideInAPackage("..api..")
         .allowEmptyShould(true);

      rule.check(classes);
   }

   @Test
   @DisplayName("Application layer should not depend on infrastructure layer")
   void applicationShouldNotDependOnInfrastructure() {
      ArchRule rule = noClasses().that()
         .resideInAPackage("..application..")
         .should()
         .dependOnClassesThat()
         .resideInAPackage("..infrastructure..")
         .allowEmptyShould(true);

      rule.check(classes);
   }

   @Test
   @DisplayName("Application layer should not depend on API layer")
   void applicationShouldNotDependOnApi() {
      ArchRule rule = noClasses().that()
         .resideInAPackage("..application..")
         .should()
         .dependOnClassesThat()
         .resideInAPackage("..api..")
         .allowEmptyShould(true);

      rule.check(classes);
   }

   @Test
   @DisplayName("Infrastructure layer should not depend on API layer")
   void infrastructureShouldNotDependOnApi() {
      ArchRule rule = noClasses().that()
         .resideInAPackage("..infrastructure..")
         .should()
         .dependOnClassesThat()
         .resideInAPackage("..api..")
         .allowEmptyShould(true);

      rule.check(classes);
   }
}
