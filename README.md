# matoo-api

Spring Cloud microservices backend written in Kotlin (reactive: WebFlux + coroutines).
Five runnable services plus one shared library, composed as a single Gradle multi-project build.

For the full contributor guide see [AGENTS.md](./AGENTS.md). Each service also keeps an
up-to-date package tree in its own `*.md` (`auth/auth.md`, `user/user.md`, `board/board.md`).

## Services

| Module    | Package             | Port | Role |
|-----------|---------------------|------|------|
| `core`    | `com.matoo.core`    | —    | Shared library (no bootJar): events, ports, JWT, tracing, exception handling, WebClient config |
| `eureka`  | `com.matoo.eureka`  | 8761 | Service discovery (Netflix Eureka server) |
| `gateway` | `com.matoo.gateway` | 8080 | Spring Cloud Gateway: routing, CORS, JWT auth |
| `auth`    | `com.matoo.auth`    | 8081 | Login / OAuth (Google, Apple) / token issuance |
| `user`    | `com.matoo.user`    | 8082 | Users, terms, email (SMTP/SES), web push |
| `board`   | `com.matoo.board`   | 8083 | Posts, comments, file uploads (S3/Supabase) |

## Tech stack

- Java 21, Kotlin 2.2.21, Spring Boot 3.5.0, Spring Cloud 2025.0.0
- Reactive web: Spring WebFlux + Kotlin coroutines
- Persistence: JPA / Hibernate over PostgreSQL (MySQL driver also on classpath), schema managed by Flyway
- Reads on the board service use QueryDSL (list aggregate + detail projection)
- Async messaging: Redis Streams (`core.event.*`); sync calls: load-balanced `WebClient` (`lb://`) with Resilience4j
- Build: Gradle multi-project, version catalog in `gradle/libs.versions.toml`

## Architecture

- Hexagonal (ports and adapters) per service: `adapter` -> `application` (ports) -> `domain`.
  Controllers call `port/in` use cases; out-adapters implement `port/out`.
- Asymmetric JWT: `auth` holds the private key and issues tokens; `gateway` holds the public key
  and verifies them, then injects `X-User-Id` / `X-Request-Id` into downstream requests.
  Downstream services trust these headers (they do not re-validate the JWT).
- Whitelisted paths (e.g. `/api/v1/auth/**`, `/api/v1/board/public/**`) and `OPTIONS` skip auth.

## Prerequisites

- JDK 21
- A `.env` file at the repo root (see `.env.sample` for the full key list: per-service DB URLs,
  JWT keypair, OAuth, AWS S3/SES, Redis, VAPID, SMTP). Loaded via `dotenv-kotlin`.
- Running PostgreSQL and Redis instances reachable from the values in `.env`.

## Run locally

Start in dependency order: `eureka` first, then `gateway`, then `auth` / `user` / `board`
(services register with Eureka at `localhost:8761`).

```bash
# Run a service with the local profile
./gradlew :user:bootRun --args="--spring.profiles.active=local"

# Or build a runnable jar and run it
./gradlew :user:bootJar
java -jar user/build/libs/user-0.0.1-SNAPSHOT.jar
```

> Always use the root `./gradlew` so module dependencies (e.g. `:core`) resolve. Each module
> also has its own wrapper, but the root build is the supported entry point.

## Docker

```bash
docker build -t matoo-api/user:latest -f user/Dockerfile user
docker run --env-file .env -p 8082:8082 matoo-api/user:latest
```

## Common commands

```bash
# Build / test
./gradlew build                 # build everything
./gradlew :board:bootJar        # one service's runnable jar
./gradlew test                  # all modules
./gradlew :board:test           # one module

# Housekeeping
./gradlew --stop                # stop the Gradle daemon
./gradlew clean
./gradlew clean --no-build-cache --no-configuration-cache
./gradlew tasks
```
