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
    // note: this is a tests-only project and all classes are in main source set
    implementation("software.amazon.awssdk:s3:2.18.31")
    implementation("software.amazon.awssdk:lambda:2.18.31")
    implementation("com.amazonaws:aws-java-sdk-cognitoidp:1.12.361")
    implementation("org.apache.httpcomponents:httpmime:4.5.14")
    implementation("org.json:json:20220924")
    implementation("org.junit.jupiter:junit-jupiter:5.9.1")
    implementation("org.assertj:assertj-core:3.23.1")
    implementation("org.skyscreamer:jsonassert:1.5.1")
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

        // output
        testlogger {
            showStandardStreams = true
            showPassedStandardStreams = false
        }

    }

}