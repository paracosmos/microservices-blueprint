
### run services
```bash
#./gradlew :user:build
./gradlew :user:bootJar

# ./gradlew :user:bootRun --args="--spring.profiles.active=local"
cd .\user\build\libs\
java -jar user-0.0.1-SNAPSHOT.jar

#./gradlew :user:bootBuildImage
docker build -t matoo-api/user:latest -f user/Dockerfile user
docker run --env-file .env -p 8082:8082 matoo-api/user:latest
```

### scripts
```bash
./gradlew --version
./gradlew tasks
./gradlew clean --no-build-cache --no-configuration-cache

./gradlew --stop
./gradlew clean
./gradlew build
```

### version
```kotlin
plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.spring") version "2.2.21"
    id("org.springframework.boot") version "3.5.0"
    id("io.spring.dependency-management") version "1.1.7"
}
```

## Service Overview
| service     | package             | port |
|-------------|---------------------|------|
| **Common**  | `com.matoo.common`  |      |
| **Eureka**  | `com.matoo.eureka`  | 8761 |
| **Gateway** | `com.matoo.gateway` | 8080 |
| **Auth**    | `com.matoo.auth`    | 8081 |
| **User**    | `com.matoo.user`    | 8082 |
| **Board**   | `com.matoo.board`   | 8083 |
