dependencies {

    // Code shared by all functions, including AWS libraries.
    // Note: The shared library has to be built and installed first.
    implementation("org.jarhc.online:shared:1.0.0-SNAPSHOT")

    // JarHC - JAR Health Check
    // https://github.com/smarkwal/jarhc
    implementation("org.jarhc:jarhc:2.0.0")
}
