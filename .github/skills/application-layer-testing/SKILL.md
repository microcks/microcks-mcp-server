---
name: application-layer-testing
description: Use when testing use cases in hexagonal architecture to cover domain without writing separate domain unit tests
---

# Application Layer Testing

## Overview

Test use cases in isolation by mocking ports to achieve **100% domain coverage indirectly**. Domain objects (Result types, value objects, exceptions) are exercised through use case tests - no separate domain unit tests needed.

**Core insight:** Testing the application layer automatically covers the domain layer it uses.

## When to Use

- Testing `*UseCase` classes in hexagonal architecture
- Want to test **behavior/functionality**, not implementation details
- Need 100% coverage on domain layer without writing domain-specific tests
- Testing application layer that returns `Result<T>` types
- Want to cover domain objects (exceptions, value objects) indirectly

**Do NOT use for:**
- Infrastructure adapter tests (use integration tests with Testcontainers)
- API/Controller adapter tests (use endpoint integration tests)
- Writing separate unit tests for domain objects (they're covered here)
- Testing internal implementation details (test behavior instead)

## Core Pattern

```
┌─────────────────────────────────────────────────────────────┐
│  Application Layer Test                                     │
│                                                             │
│  @QuarkusTest / @SpringBootTest                             │
│  ┌─────────────────┐    ┌───────────────────────────────┐  │
│  │ @InjectMock     │───▶│ Port (Interface)              │  │
│  │ @MockBean       │    │ - Mock returns/throws         │  │
│  └─────────────────┘    └───────────────────────────────┘  │
│           │                                                 │
│           ▼                                                 │
│  ┌─────────────────┐    ┌───────────────────────────────┐  │
│  │ @Inject         │───▶│ UseCase                       │  │
│  │ @Autowired      │    │ - Real implementation         │  │
│  └─────────────────┘    └───────────────────────────────┘  │
│           │                                                 │
│           ▼                                                 │
│  ┌─────────────────────────────────────────────────────┐   │
│  │ Result<T> → Pattern Matching Assertions              │   │
│  └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

## Quick Reference

| Category | Test Cases Required |
|----------|-------------------|
| **Success** | Empty (0), Single (1), Multiple (N) |
| **Errors** | One per exception type in catch blocks |
| **Domain** | Null validation, type constraints |
| **Delegation** | Verify port called exactly once |

## Implementation Checklist

### 1. Test Class Setup

**Quarkus:**
```java
@QuarkusTest
class OrderUseCaseTest {

   @Inject
   OrderUseCase useCase;  // Real implementation

   @InjectMock
   OrderRepositoryPort port;    // Mocked port interface
}
```

**Spring Boot:**
```java
@SpringBootTest
class OrderUseCaseTest {

   @Autowired
   OrderUseCase useCase;  // Real implementation

   @MockBean
   OrderRepositoryPort port;    // Mocked port interface
}
```

### 2. Success Cases - Cover Cardinalities (0, 1, N)

```java
// Empty list (0 elements)
@Test
void shouldReturnSuccessWithEmptyListWhenRepositoryReturnsEmpty() {
   when(port.findAll()).thenReturn(Collections.emptyList());
   
   Result<List<Order>> result = useCase.listOrders();
   
   switch (result) {
      case Result.Success(var orders) -> assertTrue(orders.isEmpty());
      case Result.Error(var msg) -> fail("Expected success: " + msg);
   }
}

// Single element (1)
@Test
void shouldReturnSuccessWithSingleOrder() { ... }

// Multiple elements (N)
@Test
void shouldReturnSuccessWithMultipleOrders() { ... }
```

### 3. Error Cases - One Per Exception Type

Map each `catch` block in the use case to a test:

```java
// UseCase code:
catch (UnauthorizedException e) { ... }
catch (ForbiddenException e) { ... }
catch (AccessException e) { ... }
catch (Exception e) { ... }

// Tests needed:
@Test void shouldReturnErrorWhenUnauthorized() { ... }
@Test void shouldReturnErrorWhenForbidden() { ... }
@Test void shouldReturnErrorForGenericAccessException() { ... }
@Test void shouldReturnErrorForUnexpectedRuntimeException() { ... }
```

### 4. Pattern Matching Assertions (Java 21+)

```java
// Assert success with value extraction
switch (result) {
   case Result.Success(var services) -> {
      assertEquals(3, services.size());
      assertSame(expected, services.get(0));
   }
   case Result.Error(var message) -> fail("Expected success: " + message);
}

// Assert error with message validation
switch (result) {
   case Result.Success(var _) -> fail("Expected error");
   case Result.Error(var message) -> {
      assertTrue(message.contains("authentication"));
      assertTrue(message.contains("Do not retry"));
   }
}
```

### 5. Domain Coverage (Indirect)

Domain factory methods and constraints get tested when use cases exercise them:

```java
@Test
void successResultShouldRejectNullValue() {
   assertThrows(NullPointerException.class, 
      () -> Result.success(null));
}

@Test
void errorResultShouldRejectNullMessage() {
   assertThrows(NullPointerException.class, 
      () -> Result.error(null));
}
```

**Note:** These tests can live in the application test class - no separate domain test file needed.

### 6. Delegation Verification

```java
@Test
void shouldDelegateToPortExactlyOnce() {
   when(port.findAll()).thenReturn(List.of(...));
   
   useCase.listOrders();
   
   verify(port, times(1)).findAll();
   verifyNoMoreInteractions(port);
}
```

## Naming Convention

```
should[ExpectedBehavior]When[Condition]

Examples:
- shouldReturnSuccessWithOrdersWhenRepositoryReturnsMultipleOrders
- shouldReturnErrorWithNoRetryGuidanceWhenUnauthorized
- shouldDelegateToPortExactlyOnce
```

## Coverage Strategy

| Layer | Tested Via | Target |
|-------|-----------|--------|
| `domain/` | **Indirectly** via application tests | 100% |
| `application/` | UseCase tests with mocked ports | 100% |
| `api/` | Endpoint integration tests | 80%+ |
| `infrastructure/` | Testcontainers integration tests | 80%+ |

**Key insight:** Domain objects (Result, exceptions, value objects) get exercised when testing use cases. Writing separate domain unit tests is redundant - the application tests already cover them.

## Common Mistakes

| Mistake | Fix |
|---------|-----|
| Writing separate domain unit tests | Domain is covered indirectly via application tests |
| Testing infrastructure in use case tests | Mock ports only, never real adapters |
| Missing empty list case | Always test 0, 1, N cardinalities |
| Missing exception type | One test per catch block |
| Using `assertTrue(result.isSuccess())` | Use pattern matching for type-safe extraction |
| Testing API adapters with `@Inject` only | Use endpoint integration tests |
| Forgetting `verify()` | Always verify delegation to ports with `times(1)` |

## File Structure

```
src/test/java/com/example/
├── application/
│   └── order/
│       └── OrderUseCaseTest.java         ← This skill
├── api/
│   └── OrderControllerTest.java          ← API adapter tests
├── infrastructure/
│   └── OrderRepositoryIT.java            ← Integration tests
└── architecture/
    └── HexagonalArchitectureTest.java    ← ArchUnit rules
```
