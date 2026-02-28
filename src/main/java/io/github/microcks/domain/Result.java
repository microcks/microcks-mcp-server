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

import java.util.Objects;


/**
 * Domain result type that encapsulates either a success value or an error with an AI-friendly message.
 * 
 * <pre>{@code
 * return switch (result) {
 *    case Result.Success(var value) -> handleSuccess(value);
 *    case Result.Error(var message) -> handleError(message);
 * };
 * }</pre>
 *
 * @param <T> The type of the success value
 */
public sealed interface Result<T> {

   // --- Factory methods ---

   /**
    * Create a success result with the given value.
    *
    * @param value the success value (must not be null)
    * @param <T>   the type of the value
    * @return a success result
    */
   static <T> Result<T> success(T value) {
      return new Success<>(value);
   }

   /**
    * Create an error result with the given AI-friendly message.
    *
    * @param errorMessage the error message for AI agents (must not be null)
    * @param <T>          the expected success type
    * @return an error result
    */
   static <T> Result<T> error(String errorMessage) {
      return new Error<>(errorMessage);
   }

   // --- Implementations ---

   /**
    * Success case - contains the successful value.
    *
    * @param value the success value
    */
   record Success<T>(T value) implements Result<T> {

      public Success {
         Objects.requireNonNull(value, "Success value cannot be null");
      }
   }

   /**
    * Error case - contains an AI-friendly error message with retry/abandon guidance.
    *
    * @param errorMessage the error message for AI agents
    */
   record Error<T>(String errorMessage) implements Result<T> {

      public Error {
         Objects.requireNonNull(errorMessage, "Error message cannot be null");
      }
   }
}
