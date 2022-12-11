dependencies {

    // Code shared by all functions, including AWS libraries.
    // Note: The shared library has to be built and installed first.
    implementation(project(":shared"))

    // AWS Lambda Java Runtime Interface Client
    // Interface between Lambda service and function code.
    // Entrypoint in Docker image, see Dockerfile.
    // https://github.com/aws/aws-lambda-java-libs/tree/master/aws-lambda-java-runtime-interface-client
    implementation("com.amazonaws:aws-lambda-java-runtime-interface-client:2.1.1")

    // test dependencies -------------------------------------

    testImplementation(testFixtures(project(":shared")))
}

tasks {

    register("copyDependencies", Copy::class) {
        group = "build"
        from(configurations.runtimeClasspath)
        into("$buildDir/dependencies")
    }

    build {
        dependsOn("copyDependencies")
    }

}