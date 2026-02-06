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

kotlin {
    jvmToolchain(21)
    sourceSets {
        val main by getting {
            dependencies {
                implementation("io.ktor:ktor-server-core")
                implementation("io.ktor:ktor-server-netty")
            }
        }
    }
}
