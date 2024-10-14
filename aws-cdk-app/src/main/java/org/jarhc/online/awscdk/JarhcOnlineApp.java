package org.jarhc.online.awscdk;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

public class JarhcOnlineApp {

	private static final String APP_NAME = "jarhc-online";

	private static final String AWS_ACCOUNT_ID = "837783538267";
	private static final String AWS_REGION_EU_CENTRAL_1 = "eu-central-1";
	private static final String AWS_REGION_US_EAST_1 = "us-east-1";

	public static void main(final String[] args) {

		App app = new App();

		StackProps props1 = buildStackProps(AWS_REGION_EU_CENTRAL_1);
		new ApiStack(app, APP_NAME + "-api", props1);
		new CognitoStack(app, APP_NAME + "-cognito", props1);
		new WebsiteStack(app, APP_NAME + "-website", props1);

		StackProps props2 = buildStackProps(AWS_REGION_US_EAST_1);
		new CertStack(app, APP_NAME + "-cert", props2);

		app.synth();

	}

	private static StackProps buildStackProps(String region) {

		Environment environment = Environment.builder()
				.account(AWS_ACCOUNT_ID)
				.region(region)
				.build();

		return StackProps.builder()
				.env(environment)
				.build();
	}

}
