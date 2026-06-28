plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)

    alias(libs.plugins.kotlin.allopen)
    alias(libs.plugins.kotlin.noarg)
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.kotlin.kapt)
}

description = "board"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

dependencies {
    implementation(project(":core"))
    implementation(libs.bundles.kotlinxEcosystem)
    implementation(libs.bundles.springFramework)

    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")

    // repository
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.postgresql:postgresql")
//    implementation("com.mysql:mysql-connector-j")

    // querydsl (version managed by Spring Boot BOM)
    implementation("com.querydsl:querydsl-jpa::jakarta")
    kapt("com.querydsl:querydsl-apt::jakarta")
    kapt("jakarta.annotation:jakarta.annotation-api")
    kapt("jakarta.persistence:jakarta.persistence-api")

    // flyway
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")
//    implementation("org.flywaydb:flyway-mysql")

    // aws
    implementation("software.amazon.awssdk:s3")
    implementation("software.amazon.awssdk:apache-client")
    implementation("software.amazon.awssdk:netty-nio-client")

    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:${libs.versions.springBootVersion.get()}")
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${libs.versions.springCloudVersion.get()}")
        mavenBom("io.github.resilience4j:resilience4j-bom:${libs.versions.resilience4j.get()}")
        mavenBom("software.amazon.awssdk:bom:${property("awssdk")}")
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

noArg {
    annotation("jakarta.persistence.Entity")
}

tasks.jar {
    enabled = false
}

tasks.bootJar {
    enabled = true
}
