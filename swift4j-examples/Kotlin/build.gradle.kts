plugins {
    java
    application
    kotlin("jvm") version "2.0.10"
    id("io.scade.gradle.plugins.swiftpm") version "latest.release"
}

group = "org.swift.examples.kotlin"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass = "MainKt"
}

swiftpm {
    path = file("../../Packages/swift4j-examples")
    product = "swift4j-examples"
    scdAutoUpdate = true
}