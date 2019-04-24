import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    kotlin("jvm") version "1.3.30"
}

group = "eli.quarut"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}


val kotlinVersion = "1.3.30"
val junitPlatformVersion = "1.4.1"
val junitJupiterVersion = "5.4.1"

dependencies {
    implementation(kotlin("stdlib-jdk8", kotlinVersion))
    implementation(kotlin("reflect", kotlinVersion))
    implementation("com.google.guava:guava:27.1-jre")

    testImplementation(kotlin("test", kotlinVersion))
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
    testImplementation("org.junit.platform:junit-platform-runner:$junitPlatformVersion")
    testImplementation("com.github.moove-it:fakeit:v0.7")

    testRuntime("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
    testRuntime("org.junit.platform:junit-platform-engine:$junitPlatformVersion")
    testRuntime("org.junit.platform:junit-platform-launcher:$junitPlatformVersion")
}

tasks.withType<Delete> {
    delete(rootProject.buildDir)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "12"
}

tasks.getting(Test::class) {
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
        events("FAILED", "SKIPPED", "PASSED")
        showStandardStreams = true
    }
    useJUnitPlatform {
        includeEngines("junit-jupiter")
    }
}