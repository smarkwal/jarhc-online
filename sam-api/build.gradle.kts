plugins {
    java

    // Gradle Versions Plugin
    // https://github.com/ben-manes/gradle-versions-plugin
    id("com.github.ben-manes.versions") version "0.44.0"

    // Gradle Test Logger Plugin
    // https://github.com/radarsh/gradle-test-logger-plugin
    id("com.adarshr.test-logger") version "3.2.0"
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

}