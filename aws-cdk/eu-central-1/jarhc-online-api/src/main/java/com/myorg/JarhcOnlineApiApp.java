package com.myorg;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

public class JarhcOnlineApiApp {

	public static void main(final String[] args) {

		App app = new App();
		new JarhcOnlineApiStack(app, "jarhc-online-api",
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
