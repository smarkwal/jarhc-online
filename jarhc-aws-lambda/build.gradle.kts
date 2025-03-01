plugins {
    java
    idea

    // Gradle Versions Plugin
    // https://github.com/ben-manes/gradle-versions-plugin
    id("com.github.ben-manes.versions") version "0.52.0"

    // Gradle Test Logger Plugin
    // https://github.com/radarsh/gradle-test-logger-plugin
    id("com.adarshr.test-logger") version "4.0.0"

    // JarHC Gradle plugin
    id("org.jarhc") version "1.1.1"
}

// Java version check ----------------------------------------------------------

if (!JavaVersion.current().isCompatibleWith(JavaVersion.VERSION_17)) {
    val error = "Build requires Java 17 and does not run on Java ${JavaVersion.current().majorVersion}."
    throw GradleException(error)
}

// special settings for IntelliJ IDEA
idea {
    project {
        jdkName = "17"
        languageLevel = org.gradle.plugins.ide.idea.model.IdeaLanguageLevel(JavaVersion.VERSION_11)
        vcs = "Git"
    }
}

buildscript {
    dependencies {
        // fix CVE-2023-3635 in Okio < 3.4.0
        // (indirect dependency of Gradle Versions Plugin 0.51.0)
        classpath("com.squareup.okio:okio:3.10.2")
        classpath("com.squareup.okio:okio-jvm:3.10.2")
    }
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

dependencies {

    // BOMs for version constraints
    implementation(platform("software.amazon.awssdk:bom:2.30.11"))
    implementation(platform("com.amazonaws:aws-java-sdk-bom:1.12.780"))

    // FasterXML Jackson (transitive dependency of AWS SDK)
    // Fix CVE-2022-42003 and CVE-2022-42004 in Jackson Databind < 2.13.4.1
    implementation(platform("com.fasterxml.jackson:jackson-bom:2.18.2"))

    // logging
    implementation("org.slf4j:slf4j-api:2.0.16")

    // logging implementation
    runtimeOnly("org.slf4j:slf4j-simple:2.0.16")

    // AWS Lambda Java Runtime Interface Client
    // Interface between Lambda service and function code.
    // Entrypoint in Docker image, see Dockerfile.
    // https://github.com/aws/aws-lambda-java-libs/tree/master/aws-lambda-java-runtime-interface-client
    implementation("com.amazonaws:aws-lambda-java-runtime-interface-client:2.6.0")

    // AWS Lambda Java API
    implementation("com.amazonaws:aws-lambda-java-core:1.2.3")
    implementation("com.amazonaws:aws-lambda-java-events:3.15.0")

    // AWS SDK for S3, Lambda, and X-Ray (excluding HttpClient and Netty)
    implementation("software.amazon.awssdk:s3") {
        exclude("software.amazon.awssdk", "apache-client")
        exclude("software.amazon.awssdk", "netty-nio-client")
    }
    implementation("software.amazon.awssdk:lambda") {
        exclude("software.amazon.awssdk", "apache-client")
        exclude("software.amazon.awssdk", "netty-nio-client")
    }
    implementation("software.amazon.awssdk:url-connection-client")
    implementation("com.amazonaws:aws-xray-recorder-sdk-core:2.18.2") {
        // exclude commons-logging (replaced by jcl-over-slf4j, see below)
        exclude(group = "commons-logging", module = "commons-logging")
    }

    // JarHC - JAR Health Check
    // https://github.com/smarkwal/jarhc
    implementation("org.jarhc:jarhc:2.2.2") {
        // exclude commons-logging (replaced by jcl-over-slf4j, see below)
        exclude(group = "commons-logging", module = "commons-logging")
    }

    // test dependencies -------------------------------------

    testImplementation("org.junit.jupiter:junit-jupiter:5.11.4")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.mockito:mockito-core:5.15.2")
    testImplementation("org.mockito:mockito-junit-jupiter:5.15.2")
    testImplementation("org.assertj:assertj-core:3.27.3")
}

// exclude slf4j-simple from tests
configurations.testRuntimeClasspath {
    exclude("org.slf4j", "slf4j-simple")
}

tasks {

    clean {
        // TODO: delete(".aws-sam")
    }

    compileJava {
        options.encoding = "ASCII"
    }

    test {

        // use JUnit 5
        useJUnitPlatform()

        // settings
        maxHeapSize = "1G"
        environment("AWS_REGION", "eu-central-1")

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
        ignoreMissingAnnotations.set(true)
        reportFiles.setFrom(
            file("${projectDir}/jarhc-report.html"),
            file("${projectDir}/jarhc-report.txt")
        )
    }

    dependencyUpdates {
        gradleReleaseChannel = "current"
        rejectVersionIf {
            isUnstableVersion(candidate)
        }
    }

    // create a ZIP file with all dependencies for AWS Lambda
    register("buildZip", Zip::class) {
        group = "build"
        into("lib") {
            from(jar)
            from(configurations.runtimeClasspath)
        }
    }

    build {
        dependsOn("buildZip", jarhcReport)
    }

}


fun isUnstableVersion(candidate: ModuleComponentIdentifier): Boolean {
    return candidate.version.contains("-M") // ignore milestone version
            || candidate.version.contains("-rc") // ignore release candidate versions
            || candidate.version.contains("-alpha") // ignore alpha versions
}
