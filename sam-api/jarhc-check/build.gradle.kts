dependencies {

    // Code shared by all functions, including AWS libraries.
    // Note: The shared library has to be built and installed first.
    implementation(project(":shared")) {
        // exclude commons-logging (replaced by jcl-over-slf4j, see below)
        exclude(group = "commons-logging", module = "commons-logging")
    }

    // JarHC - JAR Health Check
    // https://github.com/smarkwal/jarhc
    implementation("org.jarhc:jarhc:2.2.0")

    // add slf4j-simple
    runtimeOnly("org.slf4j:slf4j-simple:2.0.13")

    // test dependencies -------------------------------------

    testImplementation(testFixtures(project(":shared")))
}

// exclude slf4j-simple from tests
configurations.testRuntimeClasspath {
    exclude("org.slf4j", "slf4j-simple")
}
