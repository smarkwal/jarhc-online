plugins {
    `java-library`
    `java-test-fixtures`
}

dependencies {

    // BOMs for version constraints
    api(platform("software.amazon.awssdk:bom:2.26.28"))
    api(platform("com.amazonaws:aws-java-sdk-bom:1.12.767"))

    // FasterXML Jackson (transitive dependency of AWS SDK)
    // Fix CVE-2022-42003 and CVE-2022-42004 in Jackson Databind < 2.13.4.1
    api(platform("com.fasterxml.jackson:jackson-bom:2.17.2"))

    // logging
    api("org.slf4j:slf4j-api:2.0.13")

    // AWS Lambda Java API
    api("com.amazonaws:aws-lambda-java-core:1.2.3")
    api("com.amazonaws:aws-lambda-java-events:3.13.0")

    // AWS SDK for S3, Lambda, and X-Ray (excluding HttpClient and Netty)
    api("software.amazon.awssdk:s3") {
        exclude("software.amazon.awssdk", "apache-client")
        exclude("software.amazon.awssdk", "netty-nio-client")
    }
    api("software.amazon.awssdk:lambda") {
        exclude("software.amazon.awssdk", "apache-client")
        exclude("software.amazon.awssdk", "netty-nio-client")
    }
    api("software.amazon.awssdk:url-connection-client")
    api("com.amazonaws:aws-xray-recorder-sdk-core:2.17.0")

    // test dependencies -------------------------------------

    testFixturesApi("org.junit.jupiter:junit-jupiter:5.11.0")
    testFixturesRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testFixturesApi("org.mockito:mockito-core:5.12.0")
    testFixturesApi("org.mockito:mockito-junit-jupiter:5.12.0")
    testFixturesApi("org.assertj:assertj-core:3.26.3")
}
