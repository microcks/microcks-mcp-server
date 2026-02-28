---
name: testing-mcp-endpoints
description: Use when testing Quarkus MCP Server tools via HTTP/SSE endpoints with proper MCP protocol instead of direct CDI injection
---

# Testing MCP Servers with McpAssured

## Overview

McpAssured provides a fluent API for testing MCP servers through actual MCP protocol transports (HTTP Streamable, SSE, WebSocket). Use this when you need integration tests that verify MCP protocol compliance, not just business logic.

**Core principle:** Test through the MCP protocol endpoints to verify both tool logic AND protocol handling.

## When to Use

**Use when:**
- Testing Quarkus MCP Server tools via HTTP/SSE/WebSocket
- Verifying MCP protocol compliance (JSON-RPC, tool registration, etc.)
- Integration testing that mimics real MCP client behavior
- Need to test tool discovery (`tools/list`) and execution (`tools/call`)

**Don't use when:**
- Unit testing business logic (use direct CDI injection instead)
- Testing non-MCP HTTP endpoints (use REST-assured)
- Testing without Quarkus server running (`@QuarkusTest` starts it)

## Setup

Add test dependency (no version needed - managed by Quarkus BOM):

```xml
<dependency>
  <groupId>io.quarkiverse.mcp</groupId>
  <artifactId>quarkus-mcp-server-test</artifactId>
  <scope>test</scope>
</dependency>
```

For older versions or standalone use, specify version explicitly:
```xml
<version>1.10.0</version>
```

## Core Pattern

### Basic Test Structure

```java
@QuarkusTest
class MyToolTest {
   
   @Test
   void shouldCallToolViaStreamableHttp() {
      // Arrange
      McpStreamableTestClient client = McpAssured.newConnectedStreamableClient();
      
      // Act & Assert
      client.when()
            .toolsCall("tool_name", Map.of("arg", "value"), response -> {
               assertFalse(response.isError());
               assertNotNull(response.content());
            })
            .thenAssertResults();
      
      client.disconnect();
   }
}
```

### Transport Types

| Transport | When to Use | Client Factory |
|-----------|-------------|----------------|
| **Streamable HTTP** | Default, POST `/mcp` | `McpAssured.newConnectedStreamableClient()` |
| **SSE** | Server-sent events, `/mcp/sse` | `McpAssured.newConnectedSseClient()` |
| **WebSocket** | Bidirectional, `/mcp/ws` | `McpAssured.newConnectedWebSocketClient()` |

**Recommendation:** Start with Streamable HTTP (simplest, most common).

## Common Operations

### Test Tool Registration

```java
client.when()
      .toolsList(page -> {
         var tool = page.findByName("microcks_list_services");
         assertNotNull(tool, "Tool should be registered");
         assertTrue(tool.description().contains("list"));
      })
      .thenAssertResults();
```

### Test Tool Execution

```java
client.when()
      .toolsCall("microcks_import_artifact", 
         Map.of("name", "openapi.yaml"), 
         response -> {
            assertFalse(response.isError());
            assertNotNull(response.content());
         })
      .thenAssertResults();
```

### Test Error Handling

```java
client.when()
      .toolsCall("failing_tool", Map.of(), response -> {
         assertTrue(response.isError(), "Should return error");
      })
      .thenAssertResults();
```

## Common Mistakes

| Mistake | Problem | Solution |
|---------|---------|----------|
| Using REST-assured directly | Gets HTTP 400 - invalid Accept header | Use McpAssured clients |
| Testing with `@Inject` tool adapter | Bypasses MCP protocol layer | Use McpAssured to test full stack |
| Forgetting `client.disconnect()` | Resource leaks | Always disconnect in finally/cleanup |
| Assuming Microcks is running | Tests fail with connection errors | Mock HTTP adapter or handle errors gracefully |
| Testing `/mcp` with `Accept: application/json` | HTTP 400 - MCP rejects standard headers | Use McpAssured (handles headers) |
| Trying `/mcp/sse` with POST | HTTP 405 - Method not allowed | Use SSE client, not HTTP POST |
| Testing streamable endpoint without Accept header | HTTP 400 - streamable requires specific headers | Use McpAssured streamable client |

## Common Rationalizations (Anti-Patterns)

| Rationalization | Reality | What to Do Instead |
|-----------------|---------|-------------------|
| "I'll just test the business logic with CDI injection" | Misses MCP protocol issues, tool registration, JSON-RPC errors | Test through MCP endpoints for integration tests |
| "REST-assured should work, it's just HTTP" | MCP protocol has specific header requirements REST-assured doesn't handle | Use McpAssured designed for MCP |
| "I'll figure out the right Accept header by trial and error" | Wastes 15+ minutes trying combinations | Read McpAssured docs, use the right client |
| "The example uses SSE, I can POST to /mcp/sse" | SSE is server-sent events, not POST-able | Use streamable client for `/mcp` or SSE client for `/mcp/sse` |
| "I'll skip the test since the tool works via CDI" | Tool may work but MCP registration/protocol could be broken | Always test through MCP protocol for integration tests |

## Red Flags - STOP and Use McpAssured

If you're about to:
- Use `given().post("/mcp")` with REST-assured
- Try different `Accept` header values manually
- Test MCP tools with `@Inject` only
- POST to `/mcp/sse` endpoint
- Skip testing because "it works locally"

**→ You need McpAssured.** These are signs you're about to waste time debugging protocol issues.

## Why Not REST-assured?

**Problem:** MCP endpoints reject standard HTTP requests:

```java
// ❌ FAILS: HTTP 400 - Invalid Accept header
given()
   .contentType(ContentType.JSON)
   .accept(ContentType.JSON)  // MCP rejects this
   .post("/mcp");

// ✅ WORKS: McpAssured handles MCP protocol headers
McpAssured.newConnectedStreamableClient()
   .when().toolsCall(...)
```

**Root cause:** MCP protocol uses JSON-RPC over HTTP with specific header requirements. McpAssured handles this automatically.

## Real-World Impact

**Before McpAssured:** 15 minutes debugging HTTP 400 errors, trying different headers.

**With McpAssured:** 2 minutes to write working test following documentation pattern.

**Key benefit:** Tests verify both business logic AND MCP protocol compliance in one shot.

## Reference

Full documentation: https://github.com/quarkiverse/quarkus-mcp-server/blob/main/docs/modules/ROOT/pages/guides-testing.adoc
