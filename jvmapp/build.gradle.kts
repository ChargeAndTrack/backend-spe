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
    implementation(platform("org.mongodb:mongodb-driver-bom:5.6.1"))
    implementation("org.mongodb:mongodb-driver-kotlin-coroutine")
    implementation("org.mongodb:bson-kotlinx")
}

kotlin {
    jvmToolchain(21)
}
