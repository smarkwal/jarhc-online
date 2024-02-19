plugins {
    java
    idea

    // Gradle Versions Plugin
    // https://github.com/ben-manes/gradle-versions-plugin
    id("com.github.ben-manes.versions") version "0.51.0"

    // Gradle Test Logger Plugin
    // https://github.com/radarsh/gradle-test-logger-plugin
    id("com.adarshr.test-logger") version "4.0.0"

    // JarHC Gradle plugin
    id("org.jarhc") version "1.0.1"
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

        dependencyUpdates {
            rejectVersionIf {
                isUnstableVersion(candidate)
            }
        }

    }

}

fun isUnstableVersion(candidate: ModuleComponentIdentifier): Boolean {
    return candidate.version.contains("-M") // ignore milestone version
            || candidate.version.contains("-rc") // ignore release candidate versions
            || candidate.version.contains("-alpha") // ignore alpha versions
}
