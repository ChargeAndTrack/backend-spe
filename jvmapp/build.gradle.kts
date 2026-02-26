import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    kotlin("jvm") version "2.3.10"
    kotlin("plugin.serialization") version "2.3.10"
    application
    id("io.ktor.plugin") version "3.4.0"
}

application {
    mainClass = "infrastructure.AppKt"
}

ktor {
    fatJar {
        archiveFileName.set("jvmapp.jar")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-netty")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("io.ktor:ktor-server-auth")
    implementation("io.ktor:ktor-server-auth-jwt")
    implementation("io.ktor:ktor-server-status-pages")
    implementation(platform("org.mongodb:mongodb-driver-bom:5.6.1"))
    implementation("org.mongodb:mongodb-driver-kotlin-coroutine")
    implementation("org.mongodb:bson-kotlinx")

    testImplementation("io.kotest:kotest-runner-junit5-jvm:6.1.4")
    testImplementation("io.kotest:kotest-assertions-core-jvm:6.1.4")
    testImplementation("io.ktor:ktor-client-cio")
    testImplementation("io.ktor:ktor-client-content-negotiation")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
        events("passed", "skipped", "failed")
    }
}

kotlin {
    jvmToolchain(21)
}
