plugins {
    `java-library`
    `java-test-fixtures`
}

dependencies {

    // BOMs for version constraints
    api(platform("software.amazon.awssdk:bom:2.18.35"))
    api(platform("org.apache.logging.log4j:log4j-bom:2.19.0"))

    // AWS Lambda Java API and logging
    api("com.amazonaws:aws-lambda-java-core:1.2.2")
    api("com.amazonaws:aws-lambda-java-events:3.11.0")
    api("com.amazonaws:aws-lambda-java-log4j2:1.5.1")
    api("org.apache.logging.log4j:log4j-slf4j18-impl:2.18.0")

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
    api("com.amazonaws:aws-xray-recorder-sdk-core:2.13.0")

    // test dependencies -------------------------------------

    testFixturesApi("org.junit.jupiter:junit-jupiter:5.9.1")
    testFixturesApi("org.mockito:mockito-core:4.9.0")
    testFixturesApi("org.mockito:mockito-junit-jupiter:4.9.0")
    testFixturesApi("org.assertj:assertj-core:3.23.1")
}
