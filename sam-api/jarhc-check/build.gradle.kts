dependencies {

    // Code shared by all functions, including AWS libraries.
    // Note: The shared library has to be built and installed first.
    implementation(project(":shared")) {
        // exclude commons-logging (replaced by jcl-over-slf4j, see below)
        exclude(group = "commons-logging", module = "commons-logging")
    }

    // JarHC - JAR Health Check
    // https://github.com/smarkwal/jarhc
    implementation("org.jarhc:jarhc:2.0.0") {
        // exclude SLF4J 1.7 (replaced by SLF4J 2.0, see below)
        // TODO: remove when JarHC depends on SLF4J 2.0
        exclude(group = "org.slf4j")
    }

    // add SLF4J 2.0
    implementation("org.slf4j:jcl-over-slf4j:2.0.6")
    implementation("org.slf4j:jul-to-slf4j:2.0.6")
    runtimeOnly("org.slf4j:slf4j-simple:2.0.6")

    // test dependencies -------------------------------------

    testImplementation(testFixtures(project(":shared")))
}

// exclude slf4j-simple from tests
configurations.testRuntimeClasspath {
    exclude("org.slf4j", "slf4j-simple")
}
