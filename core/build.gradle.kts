plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

description = "core"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

dependencies {
    implementation(libs.bundles.kotlinxEcosystem)
    implementation(libs.bundles.springFramework)

    api("org.springframework.cloud:spring-cloud-starter-loadbalancer")

    // jackson
    api("com.fasterxml.jackson.module:jackson-module-kotlin")
    api("com.fasterxml.jackson.core:jackson-databind")

    // env
    api("io.github.cdimascio:dotenv-kotlin:${property("dotenv")}")
    // ulid
    implementation("com.github.f4b6a3:ulid-creator:${property("ulid")}")
    // jwt
    api("io.jsonwebtoken:jjwt-api:${property("jjwt")}")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${property("jjwt")}")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${property("jjwt")}")
    // micrometer-tracing
    api("io.micrometer:micrometer-observation")
    api("io.micrometer:micrometer-tracing")
    api("io.micrometer:micrometer-tracing-bridge-brave")
    api("io.micrometer:context-propagation")
    // logback
    api("net.logstash.logback:logstash-logback-encoder:${property("logbackEncoder")}")

}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:${libs.versions.springBootVersion.get()}")
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${libs.versions.springCloudVersion.get()}")
    }
}

kotlin {
    jvmToolchain(21)
}

tasks.jar {
    enabled = false
}

tasks.bootJar {
    enabled = false
}
