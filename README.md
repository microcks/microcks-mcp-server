# Microcks MCP Server

MCP Server for interacting with a Microcks instance from an Agent/MCP Client.

[![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/microcks/microcks-mcp-server/build-verify.yml?logo=github&style=for-the-badge)](https://github.com/microcks/microcks/actions)
[![License](https://img.shields.io/github/license/microcks/microcks-mcp-server?style=for-the-badge&logo=apache)](https://www.apache.org/licenses/LICENSE-2.0)
[![Project Chat](https://img.shields.io/badge/discord-microcks-pink.svg?color=7289da&style=for-the-badge&logo=discord)](https://microcks.io/discord-invite/)
[![CNCF Landscape](https://img.shields.io/badge/CNCF%20Landscape-5699C6?style=for-the-badge&logo=cncf)](https://landscape.cncf.io/?item=app-definition-and-development--application-definition-image-build--microcks)

## Build Status

The current development version is `0.0.1-SNAPSHOT`.

#### OpenSSF best practices on Microcks core

[![CII Best Practices](https://bestpractices.coreinfrastructure.org/projects/7513/badge)](https://bestpractices.coreinfrastructure.org/projects/7513)
[![OpenSSF Scorecard](https://api.securityscorecards.dev/projects/github.com/microcks/microcks/badge)](https://securityscorecards.dev/viewer/?uri=github.com/microcks/microcks)

## Community

* [Documentation](https://microcks.io/documentation/tutorials/getting-started/)
* [Microcks Community](https://github.com/microcks/community) and community meeting
* Join us on [Discord](https://microcks.io/discord-invite/), on [GitHub Discussions](https://github.com/orgs/microcks/discussions) or [CNCF Slack #microcks channel](https://cloud-native.slack.com/archives/C05BYHW1TNJ)

To get involved with our community, please make sure you are familiar with the project's [Code of Conduct](./CODE_OF_CONDUCT.md).

## How to build Microcks MCP Server?

This project is using Java 25. Be sure to have a JDK 25 installed on your machine.

Run it locally with: `./mvnw clean quarkus:dev`

```sh
$ ./mvnw clean quarkus:dev                                                                                                                                                                                                           ─╯
[INFO] Scanning for projects...
[INFO] 
[INFO] ---------------< io.github.microcks:microcks-mcp-server >---------------
[INFO] Building microcks-mcp-server 0.0.1-SNAPSHOT
[INFO]   from pom.xml
[INFO] ------------------------------[ quarkus ]-------------------------------
[...]
[INFO] Invoking compiler:3.14.1:testCompile (default-testCompile) @ microcks-mcp-server
[INFO] Recompiling the module because of changed dependency.
Listening for transport dt_socket at address: 5005
__  ____  __  _____   ___  __ ____  ______ 
 --/ __ \/ / / / _ | / _ \/ //_/ / / / __/ 
 -/ /_/ / /_/ / __ |/ , _/ ,< / /_/ /\ \   
--\___\_\____/_/ |_/_/|_/_/|_|\____/___/   
2026-02-17 15:11:30,642 WARN  [io.quarkiverse.mcp.server.http.runtime.StreamableHttpMcpMessageHandler] (Quarkus Main Thread) Cross-Origin Resource Sharing (CORS) filter must be enabled for Streamable HTTP MCP server endpoints  with `quarkus.http.cors.enabled=true`                                                                                                                                                                                                          
                                                                                                                                                                                                                                         
2026-02-17 15:11:30,703 INFO  [io.quarkus] (Quarkus Main Thread) microcks-mcp-server 0.0.1-SNAPSHOT on JVM (powered by Quarkus 3.31.3) started in 1.078s. Listening on: http://localhost:8080
2026-02-17 15:11:30,703 INFO  [io.quarkiverse.mcp.server] (executor-thread-1) MCP HTTP transport endpoints [streamable: http://localhost:8080/mcp, SSE: http://localhost:8080/mcp/sse]
2026-02-17 15:11:30,703 INFO  [io.quarkus] (Quarkus Main Thread) Profile dev activated. Live Coding activated.
2026-02-17 15:11:30,704 INFO  [io.quarkus] (Quarkus Main Thread) Installed features: [cdi, mcp-server-http, rest, rest-client, rest-client-jackson, smallrye-context-propagation, vertx]
```