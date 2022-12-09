plugins {
    java
    id("com.adarshr.test-logger") version ("3.2.0")
}

group = "org.jarhc.online"

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

        // output
        testlogger {
            showStandardStreams = true
            showPassedStandardStreams = false
        }

    }

}