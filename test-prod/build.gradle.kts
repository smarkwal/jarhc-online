import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    java
}

group = "org.jarhc.online"
version = "1.0.0-SNAPSHOT"

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
    implementation("software.amazon.awssdk:s3:2.18.31")
    implementation("software.amazon.awssdk:lambda:2.18.31")
    implementation("com.amazonaws:aws-java-sdk-cognitoidp:1.12.361")
    implementation("org.apache.httpcomponents:httpmime:4.5.14")
    implementation("org.json:json:20220924")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
    testImplementation("org.assertj:assertj-core:3.23.1")
    testImplementation("org.skyscreamer:jsonassert:1.5.1")
}

tasks {

    compileJava {
        options.encoding = "ASCII"
    }

    test {

        // use JUnit 5
        useJUnitPlatform()

        // settings
        maxHeapSize = "1G"

        // test task output
        testLogging {
            events = mutableSetOf(
                TestLogEvent.FAILED,
                TestLogEvent.SKIPPED,
                TestLogEvent.STANDARD_OUT,
                TestLogEvent.STANDARD_ERROR
            )
            showStandardStreams = true
            exceptionFormat = TestExceptionFormat.SHORT
            showExceptions = true
            showCauses = true
            showStackTraces = true
        }
    }

}