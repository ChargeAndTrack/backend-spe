plugins {
    kotlin("jvm") version "2.3.10"
    application
    id("io.ktor.plugin") version "3.4.0"
}

application {
    mainClass = "App.kt" 
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-netty")
    implementation(platform("org.mongodb:mongodb-driver-bom:5.6.1"))
    implementation("org.mongodb:mongodb-driver-kotlin-coroutine")
    implementation("org.mongodb:bson-kotlinx")
}

kotlin {
    jvmToolchain(21)
}
