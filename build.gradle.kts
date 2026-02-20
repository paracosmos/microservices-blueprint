plugins {
    alias(libs.plugins.kotlin.jvm) apply false
//    alias(libs.plugins.ktlint) apply false
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
//    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    group = "com.matoo"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
