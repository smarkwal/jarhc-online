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
    api("org.jarhc:jarhc:2.0.0")
}
