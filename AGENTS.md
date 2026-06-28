# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

> **Always read `.local/rule.md` before starting any work** — it holds repo-owner rules that override defaults (currently: do not add the `Co-Authored-By: Claude ...` footer to commits).

## What this is

`matoo-api` — a Spring Cloud microservices backend written in Kotlin (reactive, WebFlux + coroutines). Five runnable services plus one shared library, composed as a single Gradle multi-project build (`settings.gradle.kts`).

| Module    | Package             | Port | Role |
|-----------|---------------------|------|------|
| `core`    | `com.matoo.core`    | —    | Shared library (no bootJar) — events, ports, JWT, tracing, exception handling, WebClient config |
| `eureka`  | `com.matoo.eureka`  | 8761 | Service discovery (Netflix Eureka server) |
| `gateway` | `com.matoo.gateway` | 8080 | Spring Cloud Gateway — routing, CORS, JWT auth |
| `auth`    | `com.matoo.auth`    | 8081 | Login / OAuth (Google, Apple) / token issuance |
| `user`    | `com.matoo.user`    | 8082 | Users, terms, email (SMTP/SES), web push |
| `board`   | `com.matoo.board`   | 8083 | Posts, comments, file uploads (S3/Supabase) |

## Commands

All commands run from the repo root against the multi-project build. Each module *also* contains its own standalone `gradlew` wrapper (an artifact of how modules were scaffolded) — prefer the **root** `./gradlew` so module dependencies (e.g. `:core`) resolve.

```bash
# Build everything
./gradlew build

# Build one service's runnable jar
./gradlew :user:bootJar

# Run a service with the local profile
./gradlew :user:bootRun --args="--spring.profiles.active=local"

# Run from a built jar
java -jar user/build/libs/user-0.0.1-SNAPSHOT.jar

# Tests
./gradlew test                  # all modules
./gradlew :board:test           # one module
./gradlew :board:test --tests "com.matoo.board.BoardApplicationTests"

# Clean / housekeeping
./gradlew clean
./gradlew --stop                # stop the Gradle daemon
./gradlew clean --no-build-cache --no-configuration-cache
```

Build/configuration caching is enabled (`gradle.properties`). Toolchain: **Java 21**, Kotlin 2.2.21, Spring Boot 3.5.0, Spring Cloud 2025.0.0.

**Run order locally:** start `eureka` first, then `gateway`, then `auth` / `user` / `board` (they register with Eureka at `localhost:8761`).

### Environment / Docker

Secrets and connection strings come from a `.env` file loaded via `dotenv-kotlin` (see `.env.sample` for the full key list — DB URLs per service, JWT RSA keypairs, OAuth, AWS S3/SES, Redis, VAPID, SMTP). Build a service image with:

```bash
docker build -t matoo-api/user:latest -f user/Dockerfile user
docker run --env-file .env -p 8082:8082 matoo-api/user:latest
```

## Architecture

### Hexagonal (ports & adapters) per service

Every service module follows the same layered package layout — when adding a feature, replicate this structure rather than inventing a new one:

```
adapter/
  in/   web (controllers + dto), event listeners, scheduler   — drive the app
  out/  persistence (JPA entity/repo/mapper/adapter), client, cache, storage, event publishers — driven by the app
application/
  port/in   UseCase interfaces (entry points)
  port/out  Port interfaces the application needs (implemented by adapter/out)
  service   UseCase implementations (orchestration)
  model     commands / queries / results
domain/
  model     pure domain types
  service   domain logic
infrastructure/
  config    Spring @Configuration, properties, beans
```

Dependency direction: `adapter` → `application` (ports) → `domain`. Adapters implement `port/out` interfaces; controllers call `port/in` use cases. Out-adapter persistence classes map between JPA `*Entity` and domain models via `*Mapper`. `core` defines the generic base ports `CommandPort<T>` (`save`) and `QueryPort<T>` (`findById`).

### Authentication & request context (important cross-cutting concern)

- JWT is **RS256 asymmetric**. The `auth` service holds private keys and issues tokens; the `gateway` holds only the **access public key** and verifies tokens.
- `gateway/security/JwtAuthenticationFilter` validates the `Authorization: Bearer` token, then injects `X-User-Id` and `X-Request-Id` headers into the downstream request. Paths in `router.whitelist` (e.g. `/api/v1/auth/**`, `/api/v1/board/public/**`) and `OPTIONS` requests skip auth.
- **Downstream services trust these headers** — they do not re-validate the JWT. `core`'s `ReactiveContextFilter` reads `X-User-Id` and propagates it into both the reactive `Context` and SLF4J `MDC` for logging/tracing. Header and context key names live in `core/constant/TraceConstants`.
- All routes are prefixed `/api/v1` (`router.prefix`). The gateway explicitly returns 404 for `/user/internal/**` so internal-only controllers aren't reachable from outside.

### Inter-service communication

Two mechanisms, both via `core`:

1. **Synchronous** — load-balanced `WebClient` calls using `lb://<service>` URIs (see `core/constant/RemoteService` and `core/support/client/*`). Eureka resolves instances. `auth` calls `user` this way (`UserClientAdapter`). Resilience4j (circuit breaker + retry) wraps outbound calls in `user`.
2. **Asynchronous events** — **Redis Streams**, not Kafka (the `kafka` packages exist but are empty placeholders). Producers implement `core.event.EventPublisher` (`board`'s `RedisEventPublisher` does `XADD` of a JSON envelope keyed by topic). Consumers extend `core.event.AbstractTopicHandler` / implement `EventHandler<T>`, deserializing the envelope via `EventSerde`. Example flow: board publishes a comment-created event → user's `CommentCreatedHandler` sends a notification email/push. Topics/DTOs are defined in `core/event/dto`.

### Persistence

JPA + Hibernate over PostgreSQL and/or MySQL (drivers for both are on the classpath). `open-in-view` is disabled deliberately (see commit history). Schema is managed by **Flyway**; migration location is templated as `classpath:db/migration/<service>/${DB}` — the `DB` env var selects the database-vendor subfolder. Per-service DB credentials are separate env vars (`USER_DB_*`, `POST_DB_*`).

JPA entities rely on the Kotlin `allopen` / `noarg` / `jpa` compiler plugins (configured in each service's `build.gradle.kts`) so `@Entity`/`@Embeddable`/`@MappedSuperclass` classes get no-arg constructors and become open.

### Errors

`core.support.exception.GlobalExceptionHandler` (`@ControllerAdvice`) is the shared handler — it maps validation, `WebClient`, and `ResponseStatusException` failures to RFC 7807 `ProblemDetail` responses, unwrapping upstream service error bodies (`error`/`detail`/`reason`/`message`).

## Dependency conventions

- Version catalog lives in `gradle/libs.versions.toml` (plugins + Kotlin/Spring bundles via `libs.bundles.*`). Additional pinned versions (jjwt, flyway, awssdk, web-push, etc.) are plain properties in `gradle.properties`, referenced as `property("...")`.
- Service modules depend on `core` via `implementation(project(":core"))`. `core` re-exposes shared deps with `api(...)` so they transit to services.

## Repo conventions

- Do **not** add the `Co-Authored-By: Claude ...` footer to commit messages (per `.local/rule.md`).
- Each service has a `*.md` file (`user/user.md`, `board/board.md`, `auth/auth.md`) containing an up-to-date package tree — a quick reference for where a given class lives.
