import java.util.*

plugins {
    java

    // Gradle Versions Plugin
    // https://github.com/ben-manes/gradle-versions-plugin
    id("com.github.ben-manes.versions") version "0.50.0"

    // Gradle Test Logger Plugin
    // https://github.com/radarsh/gradle-test-logger-plugin
    id("com.adarshr.test-logger") version "4.0.0"

    // JarHC Gradle plugin
    id("org.jarhc") version "1.0.1"
}

// load user-specific properties -----------------------------------------------

val userPropertiesFile = file("${projectDir}/gradle.user.properties")
if (userPropertiesFile.exists()) {
    val userProperties = Properties()
    userProperties.load(userPropertiesFile.inputStream())
    userProperties.forEach {
        project.ext.set(it.key.toString(), it.value)
    }
}

// Java version check ----------------------------------------------------------

if (!JavaVersion.current().isJava11Compatible) {
    val error = "Build requires Java 11 and does not run on Java ${JavaVersion.current().majorVersion}."
    throw GradleException(error)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

repositories {
    mavenCentral()
}

dependencies {

    // note: this is a tests-only project and all classes are in main source set

    // BOMs for version constraints
    implementation(platform("org.junit:junit-bom:5.10.1"))
    implementation(platform("software.amazon.awssdk:bom:2.22.5"))
    implementation(platform("com.amazonaws:aws-java-sdk-bom:1.12.625"))

    // test libraries
    implementation("org.junit.jupiter:junit-jupiter")
    runtimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.assertj:assertj-core:3.24.2")
    implementation("org.skyscreamer:jsonassert:1.5.1")

    // AWS SDK for S3, Lambda, and Cognito
    implementation("software.amazon.awssdk:s3")
    implementation("software.amazon.awssdk:lambda")
    implementation("com.amazonaws:aws-java-sdk-cognitoidp")

    // logging
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("org.slf4j:slf4j-simple:2.0.11")

    // helpers
    implementation("org.apache.httpcomponents:httpmime:4.5.14")
    implementation("org.json:json:20231013")

}

tasks {

    compileJava {
        options.encoding = "ASCII"
    }

    test {

        // use JUnit 5
        useJUnitPlatform()

        // look for test classes in main source set
        testClassesDirs = sourceSets["main"].output.classesDirs

        // settings
        maxHeapSize = "1G"

        // pass all 'jarhc.*' Gradle properties as system properties to JUnit JVM
        project.properties.forEach {
            if (it.key.startsWith("jarhc.")) {
                systemProperty(it.key, it.value.toString())
            }
        }

        // pass all 'JARHC_' environment variables as system properties to JUnit JVM
        // (used in GitHub Actions to pass secrets)
        System.getenv().forEach() {
            if (it.key.startsWith("JARHC_")) {
                systemProperty(it.key.lowercase().replace('_', '.'), it.value)
            }
        }

        // output
        testlogger {
            showStandardStreams = true
            showPassedStandardStreams = false
        }

    }

    jarhcReport {
        dependsOn(jar)
        classpath.setFrom(
            jar.get().archiveFile,
            configurations.runtimeClasspath
        )
        reportFiles.setFrom(
            file("${projectDir}/jarhc-report.html"),
            file("${projectDir}/jarhc-report.txt")
        )
    }

    build {
        dependsOn(jarhcReport)
    }

}