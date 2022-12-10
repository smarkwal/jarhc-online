plugins {
    `java-library`
    `maven-publish`

    // Gradle Versions Plugin
    // https://github.com/ben-manes/gradle-versions-plugin
    id("com.github.ben-manes.versions") version "0.44.0"

    // Gradle Test Logger Plugin
    // https://github.com/radarsh/gradle-test-logger-plugin
    id("com.adarshr.test-logger") version "3.2.0"
}

group = "org.jarhc.online"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {

    // BOMs for version constraints
    api(platform("software.amazon.awssdk:bom:2.18.35"))
    api(platform("org.apache.logging.log4j:log4j-bom:2.19.0"))
    testImplementation(platform("org.junit:junit-bom:5.9.1"))

    // AWS Lambda Java API and logging
    api("com.amazonaws:aws-lambda-java-core:1.2.2")
    api("com.amazonaws:aws-lambda-java-events:3.11.0")
    api("com.amazonaws:aws-lambda-java-log4j2:1.5.1")
    api("org.apache.logging.log4j:log4j-slf4j18-impl:2.18.0")

    // AWS SDK for S3, Lambda, and X-Ray (excluding HttpClient and Netty)
    api("software.amazon.awssdk:s3") {
        exclude("software.amazon.awssdk", "apache-client")
        exclude("software.amazon.awssdk", "netty-nio-client")
    }
    api("software.amazon.awssdk:lambda") {
        exclude("software.amazon.awssdk", "apache-client")
        exclude("software.amazon.awssdk", "netty-nio-client")
    }
    api("software.amazon.awssdk:url-connection-client")
    api("com.amazonaws:aws-xray-recorder-sdk-core:2.13.0")

    testImplementation("org.junit.jupiter:junit-jupiter")
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

    register("dumpDependencies") {
        doLast {
            val dependencies = arrayListOf<String>()
            val configuration = project.configurations.getByName("runtimeClasspath")
            configuration.resolvedConfiguration.resolvedArtifacts.forEach { artifact ->
                dependencies.add(artifact.moduleVersion.id.toString())
            }
            dependencies.sort()
            file("dependencies.txt").writeText(dependencies.joinToString("\n"))
        }
    }

    build {
        dependsOn("dumpDependencies")
    }

}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
