plugins {
    `java-library`
    `java-test-fixtures`
}

dependencies {

    // BOMs for version constraints
    api(platform("software.amazon.awssdk:bom:2.19.25"))

    // logging
    api("org.slf4j:slf4j-api:2.0.6")

    // AWS Lambda Java API
    api("com.amazonaws:aws-lambda-java-core:1.2.2")
    api("com.amazonaws:aws-lambda-java-events:3.11.0")

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

    testFixturesApi("org.junit.jupiter:junit-jupiter:5.9.2")
    testFixturesApi("org.mockito:mockito-core:5.0.0")
    testFixturesApi("org.mockito:mockito-junit-jupiter:5.0.0")
    testFixturesApi("org.assertj:assertj-core:3.24.2")
}
