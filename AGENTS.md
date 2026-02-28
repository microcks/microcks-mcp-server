# AGENTS.md

## Project Overview

MCP (Model Context Protocol) Server for interacting with a Microcks instance from AI agents and MCP clients. This project enables AI coding assistants to discover and interact with Microcks mock services.

**Tech Stack:**
- Java 25 with Quarkus 3.31.3
- Quarkus MCP Server HTTP extension
- Maven with wrapper (`mvnw`)
- Hexagonal Architecture pattern

## Setup Commands

```sh
# Install dependencies and compile
./mvnw clean compile

# Start development server (with hot-reload)
./mvnw clean quarkus:dev

# Build for production
./mvnw clean package

# Build native executable
./mvnw clean package -Pnative
```

**Environment Variables:**
- `MICROCKS_API_URL`: Microcks instance URL (default: `http://localhost:8080`)

## Development Workflow

### Running the Server

```sh
./mvnw clean quarkus:dev
```

Server starts at `http://localhost:8080` with endpoints:
- Streamable MCP: `http://localhost:8080/mcp`
- SSE MCP: `http://localhost:8080/mcp/sse`

Live Coding is activated by default in dev mode.

### Project Structure (Hexagonal Architecture)

```
src/main/java/io/github/microcks/
├── api/            # MCP Tool adapters (ports/adapters)
├── application/    # Use cases (business logic)
├── domain/         # Domain models and Result types
└── infrastructure/ # External service adapters (Microcks HTTP client)
```

**Layer Dependencies:**
- `api` → `application` → `domain`
- `infrastructure` → `application` → `domain`
- `domain` must NOT depend on any other layer
- `application` must NOT depend on `api` or `infrastructure`

These rules are enforced by ArchUnit tests in `HexagonalArchitectureTest.java`.

## Testing Instructions

### Run All Tests

```sh
./mvnw clean test
```

### Run Specific Test Class

```sh
./mvnw test -Dtest=ListServicesToolTest
```

### Run Single Test Method

```sh
./mvnw test -Dtest=ListServicesToolTest#shouldListServicesViaStreamableHttpEndpoint
```

### Run Tests with Coverage

```sh
./mvnw clean test -Pcoverage
```

Coverage report is generated at `target/site/jacoco/index.html`.

### Test Patterns

**Test File Naming:**
- Unit tests: `*Test.java`
- Integration tests: `*IntegrationTest.java` or `*IT.java`

**Testing Frameworks:**
- JUnit 5 for unit tests
- McpAssured for MCP tool testing via HTTP endpoints
- REST-assured for HTTP endpoint testing
- Testcontainers for integration tests with real Microcks instances
- ArchUnit for architecture enforcement

**MCP Tool Testing Pattern:**
```java
@QuarkusTest
class ListServicesToolTest {
   @Test
   void shouldListServicesViaStreamableHttpEndpoint() {
      McpStreamableTestClient client = McpAssured.newConnectedStreamableClient();
      client.when()
         .toolsCall("microcks_list_services", Map.of(), response -> {
            assertNotNull(response);
         })
         .thenAssertResults();
      client.disconnect();
   }
}
```

**Integration Test with Testcontainers:**
- Use `@QuarkusTestResource(MicrocksTestResource.class)` for Microcks container
- Extend `BaseTest` for common configuration injection
- Place test artifacts in `src/test/resources/artifacts/`

## Code Style

### Formatting

Code formatting is enforced by Spotless Maven plugin using Eclipse formatter.

```sh
# Check formatting
./mvnw spotless:check

# Apply formatting
./mvnw spotless:apply
```

**Key Rules:**
- Indentation: 3 spaces
- Line width: 120 characters
- Eclipse formatter profile: `eclipse-formatter.xml`

### Conventions

- Use Java 25 features (records, pattern matching, sealed interfaces)
- Domain models should be immutable (records preferred)
- Use `Result<T>` sealed interface for error handling (not exceptions)
- Inject dependencies via CDI `@Inject`
- Use `@ApplicationScoped` for service beans
- MCP tools use `@Tool` and `@ToolArg` annotations

### License Header

All Java files must include Apache 2.0 license header:
```java
/*
 * Copyright The Microcks Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * ...
 */
```

## Build and Deployment

### JVM Build

```sh
./mvnw clean package
java -jar target/quarkus-app/quarkus-run.jar
```

### Native Build

```sh
./mvnw clean package -Pnative
./target/microcks-mcp-server-*-runner
```

### Docker Images

Dockerfiles are in `src/main/docker/`:
- `Dockerfile.jvm` - JVM-based image
- `Dockerfile.native` - Native executable image
- `Dockerfile.native-micro` - Minimal native image

## Pull Request Guidelines

### Title Format

Follow Conventional Commits:
- `fix: ` - Bug fix (PATCH release)
- `feat: ` - New feature (MINOR release)
- `docs: ` - Documentation only
- `chore: ` - Maintenance/cleanup
- `test: ` - Tests only
- `refactor: ` - Code restructuring
- `fix!:` or `feat!:` - Breaking change (MAJOR release)

### Required Checks

Before submitting a PR:

```sh
# 1. Format code
./mvnw spotless:apply

# 2. Run all tests
./mvnw clean test

# 3. Verify build
./mvnw clean package
```

### Workflow

1. Open an issue first (unless it's a typo/obvious fix)
2. Create a feature branch
3. Make changes following code style
4. Ensure all tests pass
5. Submit PR with conventional commit title

## Additional Notes

### Common Gotchas

- CORS must be enabled for MCP HTTP endpoints: `quarkus.http.cors.enabled=true`
- Testcontainers requires Docker to be running
- Integration tests (`*IntegrationTest.java`) use real Microcks containers

### Useful Commands

```sh
# Skip tests during build
./mvnw clean package -DskipTests

# Run in debug mode (port 5005)
./mvnw quarkus:dev -Ddebug

# Display dependency tree
./mvnw dependency:tree

# Update Maven wrapper
./mvnw wrapper:wrapper -Dmaven=3.9.9
```

### MCP Tool Development

When adding new MCP tools:
1. Create domain models in `domain/` package
2. Create use case in `application/` package
3. Create tool adapter in `api/` package with `@Tool` annotation
4. Add unit tests using McpAssured
5. Add integration tests with Testcontainers if needed
6. Run architecture tests to verify layer dependencies
