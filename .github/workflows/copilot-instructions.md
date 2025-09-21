# Copilot Instructions for BitVelocity Core Common Project

## Purpose
This project (`bv-core-common`) is the foundational shared library for all BitVelocity backend services. It is intended to be used **as a JAR dependency** in subsequent projects (REST APIs, WebSockets, SSE, etc.) to provide:
- **Authentication & Security**: UserContext, SecurityContextHolder, and related utilities for securing communication and enforcing authentication/authorization.
- **Business Exception Handling**: Centralized, structured exception classes for consistent error handling across services.
- **Eventing**: EventEnvelope and related event-driven architecture utilities.
- **Logging**: Common logging utilities and patterns.
- **Entities**: BaseEntity with audit fields for JPA entities.

## Usage Pattern
- **Do not** write REST APIs, WebSocket endpoints, or business logic here.
- **Do** add shared utilities, exceptions, security, and eventing code needed by all services.
- **Consume** this project as a Maven/Gradle dependency in downstream projects.

## Copilot Task Guidance
When resuming work on this project, Copilot should:
1. **Add/Enhance Shared Features**
   - Add new exceptions, logging utilities, or security helpers as needed by downstream services.
   - Extend eventing support (publishers, listeners, contract validation).
2. **Maintain Backward Compatibility**
   - Avoid breaking changes to public APIs unless versioning is updated.
3. **Document All Additions**
   - Update Javadoc and README/QUICK-START.md for any new features.
4. **Testing**
   - Ensure all new code is covered by unit tests.
   - Run `./mvnw clean test` before any release or major change.
5. **Release**
   - Build with `./mvnw clean install` to produce JARs for use in other projects.

## Example Downstream Usage
- Add as a dependency in your service's `pom.xml`:
  ```xml
  <dependency>
    <groupId>com.bit.velocity</groupId>
    <artifactId>bv-common-entities</artifactId>
    <version>1.8-SNAPSHOT</version>
  </dependency>
  <dependency>
    <groupId>com.bit.velocity</groupId>
    <artifactId>bv-common-events</artifactId>
    <version>1.8-SNAPSHOT</version>
  </dependency>
  <!-- Add others as needed -->
  ```
- Use provided classes for authentication, security, eventing, and exception handling in your REST/WebSocket/SSE services.

## When to Update This Project
- When a new cross-cutting concern (security, logging, eventing, etc.) is needed by multiple services.
- When a new type of business exception or shared entity base is required.
- When enhancing security or eventing patterns for all BitVelocity services.

## What Not To Do Here
- Do not add service-specific business logic, REST controllers, or endpoints.
- Do not add direct database migrations or service-specific configurations.

---
**Copilot: Always check QUICK-START.md and README.md for architectural alignment and usage patterns before making changes.**

