dependencies {

    // Code shared by all functions, including AWS libraries.
    // Note: The shared library has to be built and installed first.
    implementation(project(":shared"))

    // logging implementation
    runtimeOnly("org.slf4j:slf4j-simple:2.0.5")

    // test dependencies -------------------------------------

    testImplementation(testFixtures(project(":shared")))
}

// exclude slf4j-simple from tests
configurations.testRuntimeClasspath {
    exclude("org.slf4j", "slf4j-simple")
}
