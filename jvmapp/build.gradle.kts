import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    application
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotest)
    alias(libs.plugins.serialization)
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
    implementation(libs.bundles.ktor.server)
    implementation(libs.bundles.mongodb)
    testImplementation(libs.bundles.kotlin.testing)
    testImplementation(libs.bundles.ktor.testing)
}

kotlin {
    jvmToolchain(21)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
        events("passed", "skipped", "failed")
    }
}
