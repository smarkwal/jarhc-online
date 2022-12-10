plugins {
    `java-library`
}

group = "org.jarhc.online"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    api("org.jarhc.online:shared:1.0.0-SNAPSHOT")
}

tasks {

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
