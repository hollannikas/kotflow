plugins {
    kotlin("jvm") version "1.9.22"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
}

group = "com.hollannikas"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

apply(plugin = "org.jlleitschuh.gradle.ktlint")

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
