package org.jarhc.online.awscdk;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import org.jarhc.online.awscdk.stacks.ApiStack;
import org.jarhc.online.awscdk.stacks.CertStack;
import org.jarhc.online.awscdk.stacks.CognitoStack;
import org.jarhc.online.awscdk.stacks.WebsiteStack;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

public class JarhcOnlineApp {

	private static final Properties properties = new Properties();

	public static void main(final String[] args) throws IOException {

		// load configuration properties
		loadProperties("env.properties", "env.user.properties");

		String awsAccountId = getProperty("AWS_ACCOUNT_ID");
		String usEast1AwsRegion = getProperty("AWS_REGION_US_EAST_1");
		String defaultAwsRegion = getProperty("AWS_REGION_DEFAULT");
		String appName = getProperty("AppName");
		String rootDomain = getProperty("RootDomain");
		String websiteCertificateArn = getProperty("WebsiteCertificateARN");
		String cognitoCertificateArn = getProperty("CognitoCertificateARN");
		String websiteDomain = getProperty("WebsiteDomain");
		String websiteUrl = getProperty("WebsiteURL");
		String websiteBucketName = getProperty("WebsiteBucketName");
		String websiteBucketUrl = getProperty("WebsiteBucketURL");
		String websiteLogsBucket = getProperty("WebsiteLogsBucket");
		String apiDomain = getProperty("ApiDomain");
		String cognitoDomain = getProperty("CognitoDomain");
		String cognitoUserPoolArn = getProperty("CognitoUserPoolARN");
		String emailAddress = getProperty("EmailAddress");

		StackProps usEast1StackProps = buildStackProps(awsAccountId, usEast1AwsRegion);
		StackProps defaultStackProps = buildStackProps(awsAccountId, defaultAwsRegion);

		App app = new App();

		new CertStack(app, appName + "-cert", usEast1StackProps,
				websiteDomain,
				cognitoDomain,
				rootDomain
		);
		// TODO: pass CognitoCertificateARN to Cognito stack below
		// TODO: pass WebsiteCertificateARN to Website stack below

		new CognitoStack(app, appName + "-cognito", defaultStackProps,
				cognitoCertificateArn,
				cognitoDomain,
				websiteDomain,
				emailAddress,
				rootDomain
		);
		// TODO: pass CognitoUserPoolARN to API stack below

		new WebsiteStack(app, appName + "-website", defaultStackProps,
				websiteCertificateArn,
				websiteDomain,
				websiteLogsBucket,
				rootDomain
		);

		new ApiStack(app, appName + "-api", defaultStackProps,
				apiDomain,
				cognitoUserPoolArn,
				websiteUrl,
				websiteBucketName,
				websiteBucketUrl,
				rootDomain
		);

		app.synth();

	}

	private static void loadProperties(final String... filePaths) throws IOException {
		for (String filePath : filePaths) {
			if (Files.exists(Paths.get(filePath))) {
				try (Reader reader = new FileReader(filePath)) {
					properties.load(reader);
				}
			}
		}
	}

	private static String getProperty(String propertyName) {
		if (!properties.containsKey(propertyName)) {
			throw new IllegalArgumentException("Property not found: " + propertyName);
		}
		return properties.getProperty(propertyName);
	}

	private static StackProps buildStackProps(String accountId, String region) {

		Environment environment = Environment.builder()
				.account(accountId)
				.region(region)
				.build();

		return StackProps.builder()
				.env(environment)
				.build();
	}

}
