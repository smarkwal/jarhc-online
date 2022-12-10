dependencies {

    // Code shared by all functions, including AWS libraries.
    // Note: The shared library has to be built and installed first.
    implementation("org.jarhc.online:shared:1.0.0-SNAPSHOT")

    // AWS Lambda Java Runtime Interface Client
    // Interface between Lambda service and function code.
    // Entrypoint in Docker image, see Dockerfile.
    // https://github.com/aws/aws-lambda-java-libs/tree/master/aws-lambda-java-runtime-interface-client
    implementation("com.amazonaws:aws-lambda-java-runtime-interface-client:2.1.1")
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