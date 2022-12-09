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

    // Code shared by all functions, including AWS libraries.
    // Note: The shared library has to be built and installed first.
    api("org.jarhc.online:shared:1.0.0-SNAPSHOT")

    // JarHC - JAR Health Check
    // https://github.com/smarkwal/jarhc
    api("org.jarhc:jarhc:2.0.0")
}
