package com.myorg;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

public class AwsSamCliManagedDefaultApp {

	public static void main(final String[] args) {

		App app = new App();
		new AwsSamCliManagedDefaultStack(app, "aws-sam-cli-managed-default",
				StackProps.builder()
						.env(
								Environment.builder()
										.account("837783538267")
										.region("eu-central-1")
										.build()
						)
						.build()
		);
		app.synth();

	}

}
