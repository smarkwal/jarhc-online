dependencies {

    // Code shared by all functions, including AWS libraries.
    // Note: The shared library has to be built and installed first.
    implementation(project(":shared"))

    // AWS Lambda Java Runtime Interface Client
    // Interface between Lambda service and function code.
    // Entrypoint in Docker image, see Dockerfile.
    // https://github.com/aws/aws-lambda-java-libs/tree/master/aws-lambda-java-runtime-interface-client
    implementation("com.amazonaws:aws-lambda-java-runtime-interface-client:2.5.0")

    // logging implementation
    runtimeOnly("org.slf4j:slf4j-simple:2.0.12")

    // test dependencies -------------------------------------

    testImplementation(testFixtures(project(":shared")))
}

// exclude slf4j-simple from tests
configurations.testRuntimeClasspath {
    exclude("org.slf4j", "slf4j-simple")
}

tasks {

    register("copyDependencies", Copy::class) {
        group = "build"
        from(configurations.runtimeClasspath)
        into("${layout.buildDirectory.get()}/dependencies")
    }

    build {
        dependsOn("copyDependencies")
    }

}