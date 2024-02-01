plugins {
    java

    // Gradle Versions Plugin
    // https://github.com/ben-manes/gradle-versions-plugin
    id("com.github.ben-manes.versions") version "0.51.0"

    // Gradle Test Logger Plugin
    // https://github.com/radarsh/gradle-test-logger-plugin
    id("com.adarshr.test-logger") version "4.0.0"

    // JarHC Gradle plugin
    id("org.jarhc") version "1.0.1"
}

tasks {
    clean {
        delete(".aws-sam")
    }
}

subprojects {

    apply(plugin = "java")
    apply(plugin = "com.github.ben-manes.versions")
    apply(plugin = "com.adarshr.test-logger")
    apply(plugin = "org.jarhc")

    repositories {
        mavenCentral()
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(11))
        }
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
            reportFiles.setFrom(
                file("${projectDir}/jarhc-report.html"),
                file("${projectDir}/jarhc-report.txt")
            )
        }

        build {
            dependsOn(jarhcReport)
        }

    }

}