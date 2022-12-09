plugins {
    `java-library`
    `maven-publish`
    id("com.adarshr.test-logger") version ("3.2.0")
}

group = "org.jarhc.online"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    api("com.amazonaws:aws-lambda-java-core:1.2.2")
    api("com.amazonaws:aws-lambda-java-events:3.11.0")
    api("com.amazonaws:aws-lambda-java-log4j2:1.5.1")
    api("org.apache.logging.log4j:log4j-slf4j18-impl:2.18.0")
    api("software.amazon.awssdk:s3:2.18.35") {
        exclude("software.amazon.awssdk", "apache-client")
        exclude("software.amazon.awssdk", "netty-nio-client")
    }
    api("software.amazon.awssdk:lambda:2.18.32") {
        exclude("software.amazon.awssdk", "apache-client")
        exclude("software.amazon.awssdk", "netty-nio-client")
    }
    api("software.amazon.awssdk:url-connection-client:2.18.32")
    api("com.amazonaws:aws-xray-recorder-sdk-core:2.13.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
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

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
