package com.myorg;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

public class JarhcOnlineCertApp {

	public static void main(final String[] args) {

		App app = new App();
		new JarhcOnlineCertStack(app, "jarhc-online-cert",
				StackProps.builder()
						.env(
								Environment.builder()
										.account("837783538267")
										.region("us-east-1")
										.build()
						)
						.build()
		);
		app.synth();

	}

}
