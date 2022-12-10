plugins {
    `java-library`
}

group = "org.jarhc.online"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

tasks.register("copyDependencies", Copy::class) {
    group = "build"
    from(configurations.runtimeClasspath)
    into("$buildDir/dependencies")
}

tasks {
    build {
        dependsOn("copyDependencies")
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {

    // Code shared by all functions, including AWS libraries.
    // Note: The shared library has to be built and installed first.
    api("org.jarhc.online:shared:1.0.0-SNAPSHOT")

    // AWS Lambda Java Runtime Interface Client
    // Interface between Lambda service and function code.
    // Entrypoint in Docker image, see Dockerfile.
    // https://github.com/aws/aws-lambda-java-libs/tree/master/aws-lambda-java-runtime-interface-client
    api("com.amazonaws:aws-lambda-java-runtime-interface-client:2.1.1")
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
