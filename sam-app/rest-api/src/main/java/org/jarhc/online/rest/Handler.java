package org.jarhc.online.rest;

import static org.jarhc.online.rest.models.Artifact.isValidVersion;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jarhc.online.rest.clients.Lambda;
import org.jarhc.online.rest.clients.S3;
import org.jarhc.online.rest.models.Artifact;
import org.jarhc.online.rest.models.CheckResponse;
import org.jarhc.online.rest.models.JapiccCheckMessage;
import org.jarhc.online.rest.models.JapiccCheckRequest;
import org.jarhc.online.rest.models.JarhcCheckMessage;
import org.jarhc.online.rest.models.JarhcCheckRequest;
import org.jarhc.online.rest.models.MavenSearchError;
import org.jarhc.online.rest.models.MavenSearchRequest;
import org.jarhc.online.rest.models.MavenSearchResponse;
import org.jarhc.online.rest.models.User;

@SuppressWarnings({ "unused", "DuplicatedCode" })
public class Handler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private static final Logger logger = LogManager.getLogger(Handler.class);

	private static final int STATUS_OK = 200;
	private static final int STATUS_BAD_REQUEST = 400;
	private static final int STATUS_NOT_FOUND = 404;
	private static final int STATUS_INTERNAL_SERVER_ERROR = 500;

	private final S3 s3;
	private final Lambda lambda;
	private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public Handler() {

		var region = System.getenv("AWS_REGION");
		if (region == null) {
			region = "eu-central-1";
		}

		var bucketName = System.getenv("BUCKET_NAME");
		if (bucketName == null) {
			bucketName = "localhost";
		}

		var bucketUrl = System.getenv("BUCKET_URL");
		if (bucketUrl == null) {
			bucketUrl = "http://localhost:3000";
		}

		// create AWS clients
		s3 = new S3(region, bucketName, bucketUrl);
		lambda = new Lambda(region);

	}

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {

		logger.debug("Environment:\n{}", gson.toJson(System.getenv()));
		logger.debug("Request:\n{}", gson.toJson(request));
		logger.debug("Context:\n{}", gson.toJson(context));

		// get current user
		var user = getUser(request);
		logger.info("User: {}", user);

		var response = dispatch(request);

		// add CORS headers to every response
		// (reflect origin header)
		var origin = getOrigin(request);
		addCorsHeaders(response, origin);

		return response;
	}

	private User getUser(APIGatewayProxyRequestEvent request) {

		String subject = null;
		String email = null;
		String sourceIP = null;
		String userAgent = null;

		var context = request.getRequestContext();
		var authorizer = context.getAuthorizer();
		if (authorizer != null && authorizer.containsKey("claims")) {
			Object value = authorizer.get("claims");
			if (value instanceof Map) {
				@SuppressWarnings("unchecked")
				Map<String, String> claims = (Map<String, String>) value;
				subject = claims.get("sub");
				email = claims.get("email");
			}
		}

		var identity = context.getIdentity();
		if (identity != null) {
			sourceIP = identity.getSourceIp();
			userAgent = identity.getUserAgent();
		}

		return new User(subject, email, sourceIP, userAgent);
	}

	private APIGatewayProxyResponseEvent dispatch(APIGatewayProxyRequestEvent request) {

		// dispatch request to correct handler
		var path = request.getPath();
		switch (path) {
			case "/maven/search":
				return handleMavenSearch(request);
			case "/japicc/submit":
				return handleJapiccSubmit(request);
			case "/jarhc/submit":
				return handleJarhcSubmit(request);
			case "/auth/validate":
				return handleAuthValidate(request);
			default:
				return sendNotFound();
		}
	}

	private APIGatewayProxyResponseEvent handleMavenSearch(APIGatewayProxyRequestEvent request) {

		// get message from request body
		var message = request.getBody();
		logger.debug("Message: {}", message);

		// parse JSON message
		MavenSearchRequest in;
		try {
			in = gson.fromJson(message, MavenSearchRequest.class);
		} catch (Exception e) {
			logger.debug("Error parsing JSON message:", e);
			var error = new MavenSearchError(e.toString());
			return sendBody(STATUS_BAD_REQUEST, error);
		}

		// get coordinates from message
		var coordinates = in.getCoordinates();
		if (coordinates == null) {
			var error = new MavenSearchError("Missing parameter: 'coordinates'");
			return sendBody(STATUS_BAD_REQUEST, error);
		}

		// parse coordinates
		Artifact artifact;
		try {
			artifact = new Artifact(coordinates);
		} catch (Exception e) {
			var error = new MavenSearchError(e.toString());
			return sendBody(STATUS_BAD_REQUEST, error);
		}

		// check if artifact exists
		boolean exists;
		try {
			exists = artifact.exists();
		} catch (Exception e) {
			var error = new MavenSearchError(e.toString());
			return sendBody(STATUS_INTERNAL_SERVER_ERROR, error);
		}

		// prepare list of artifacts
		var artifacts = new ArrayList<Artifact>();
		if (exists) {
			artifacts.add(artifact);
		}

		// prepare response body
		var body = new MavenSearchResponse(coordinates, artifacts);

		return sendBody(STATUS_OK, body);
	}

	private APIGatewayProxyResponseEvent handleJapiccSubmit(APIGatewayProxyRequestEvent request) {

		// get message from request body
		var message = request.getBody();
		logger.debug("Message: {}", message);

		// parse JSON message
		JapiccCheckRequest in;
		try {
			in = gson.fromJson(message, JapiccCheckRequest.class);
		} catch (Exception e) {
			logger.error("Error parsing JSON message:", e);
			return sendError(STATUS_BAD_REQUEST, e);
		}

		var oldVersion = in.getOldVersion();
		logger.info("Old version: {}", oldVersion);

		// check if old version is valid
		if (!isValidVersion(oldVersion)) {
			logger.error("Version is not valid: {}", oldVersion);
			CheckResponse error = new CheckResponse(null, "Version is not valid: " + oldVersion);
			return sendBody(STATUS_BAD_REQUEST, error);
		}

		var newVersion = in.getNewVersion();
		logger.info("New version: {}", newVersion);

		// check if new version is valid
		if (!isValidVersion(newVersion)) {
			logger.error("Version is not valid: {}", newVersion);
			CheckResponse error = new CheckResponse(null, "Version is not valid: " + newVersion);
			return sendBody(STATUS_BAD_REQUEST, error);
		}

		if (oldVersion.equals(newVersion)) {
			logger.error("Submitted same version twice: {}", oldVersion);
			CheckResponse error = new CheckResponse(null, "Cannot compare version with itself: " + oldVersion);
			return sendBody(STATUS_BAD_REQUEST, error);
		}

		// calculate hash for combination of versions
		var hash = Utils.sha256hex(oldVersion + "/" + newVersion);

		// prepare report file
		var reportFileName = "report-" + hash + ".html";
		logger.debug("Report file name: {}", reportFileName);

		// check if report file already exists
		boolean exists;
		try {
			exists = s3.exists(reportFileName);
		} catch (Exception e) {
			logger.error("Error testing if report file exists in S3 bucket:", e);
			return sendError(STATUS_INTERNAL_SERVER_ERROR, e);
		}

		var reportFileURL = s3.getURL(reportFileName);
		if (exists) {
			logger.info("Report file found in S3: {}", reportFileURL);
			return sendReportFile(reportFileURL);
		}

		// check if old version exists in Maven Central
		var oldArtifact = new Artifact(oldVersion);
		exists = oldArtifact.exists();
		if (!exists) {
			logger.error("Version not found in Maven Central: {}", oldVersion);
			return sendError(STATUS_BAD_REQUEST, "Version not found in Maven Central: " + oldVersion);
		}

		// check if new version exists in Maven Central
		var newArtifact = new Artifact(newVersion);
		exists = newArtifact.exists();
		if (!exists) {
			logger.error("Version not found in Maven Central: {}", newVersion);
			return sendError(STATUS_BAD_REQUEST, "Version not found in Maven Central: " + newVersion);
		}

		var payload = new JapiccCheckMessage(oldVersion, newVersion, reportFileName);

		try {
			lambda.invokeAsync("japicc-check", payload);
		} catch (Exception e) {
			logger.error("Error invoking Lambda function:", e);
			return sendError(STATUS_INTERNAL_SERVER_ERROR, e);
		}

		return sendReportFile(reportFileURL);
	}

	private APIGatewayProxyResponseEvent handleJarhcSubmit(APIGatewayProxyRequestEvent request) {

		// get message from request body
		var message = request.getBody();
		logger.debug("Message: {}", message);

		// parse JSON message
		JarhcCheckRequest in;
		try {
			in = gson.fromJson(message, JarhcCheckRequest.class);
		} catch (Exception e) {
			logger.error("Error parsing JSON message:", e);
			return sendError(STATUS_BAD_REQUEST, e);
		}

		for (var version : in.getClasspath()) {
			logger.info("Classpath: {}", version);

			// check if version is valid
			if (!isValidVersion(version)) {
				logger.error("Version is not valid: {}", version);
				return sendError(STATUS_BAD_REQUEST, "Version is not valid: " + version);
			}

			// check if version exists in Maven Central
			var artifact = new Artifact(version);
			var exists = artifact.exists();
			if (!exists) {
				logger.error("Version not found in Maven Central: {}", version);
				return sendError(STATUS_BAD_REQUEST, "Version not found in Maven Central: " + version);
			}
		}

		for (var version : in.getProvided()) {
			logger.info("Provided: {}", version);

			// check if version is valid
			if (!isValidVersion(version)) {
				logger.error("Version is not valid: {}", version);
				return sendError(STATUS_BAD_REQUEST, "Version is not valid: " + version);
			}

			// check if version exists in Maven Central
			var artifact = new Artifact(version);
			var exists = artifact.exists();
			if (!exists) {
				logger.error("Version not found in Maven Central: {}", version);
				return sendError(STATUS_BAD_REQUEST, "Version not found in Maven Central: " + version);
			}
		}

		// calculate hash for combination of versions
		var hash = Utils.sha256hex(String.join(";", in.getClasspath()) + "/" + String.join(":", in.getProvided()));

		// prepare report file
		var reportFileName = "report-" + hash + ".html";
		logger.debug("Report file name: {}", reportFileName);

		// check if report file already exists
		boolean exists;
		try {
			exists = s3.exists(reportFileName);
		} catch (Exception e) {
			logger.error("Error testing if report file exists in S3 bucket:", e);
			return sendError(STATUS_INTERNAL_SERVER_ERROR, e);
		}

		var reportFileURL = s3.getURL(reportFileName);
		if (exists) {
			logger.info("Report file found in S3: {}", reportFileURL);
			return sendReportFile(reportFileURL);
		}

		var payload = new JarhcCheckMessage(in.getClasspath(), in.getProvided(), reportFileName);

		try {
			lambda.invokeAsync("jarhc-check", payload);
		} catch (Exception e) {
			logger.error("Error invoking Lambda function:", e);
			return sendError(STATUS_INTERNAL_SERVER_ERROR, e);
		}

		return sendReportFile(reportFileURL);
	}

	private APIGatewayProxyResponseEvent handleAuthValidate(APIGatewayProxyRequestEvent request) {

		// prepare headers
		var headers = new LinkedHashMap<String, String>();
		headers.put("Content-Type", "text/plain");

		// return API response
		return new APIGatewayProxyResponseEvent()
				.withStatusCode(STATUS_OK)
				.withHeaders(headers)
				.withBody("OK");
	}

	private String getOrigin(APIGatewayProxyRequestEvent request) {

		// get request headers
		var headers = request.getHeaders();

		// live behind AWS API Gateway
		var origin = headers.get("origin");
		if (origin != null) {
			return origin;
		}

		// test on localhost with `sam local start-api`
		origin = headers.get("Origin");
		if (origin != null) {
			return origin;
		}

		// fallback
		return "*";
	}

	private void addCorsHeaders(APIGatewayProxyResponseEvent response, String origin) {
		logger.debug("CORS Origin: {}", origin);
		var headers = response.getHeaders();
		headers.put("Access-Control-Allow-Origin", origin);
		headers.put("Access-Control-Allow-Credentials", "true");
	}

	// -------------------------------------------------------------------------

	private APIGatewayProxyResponseEvent sendReportFile(String reportFileURL) {
		var body = new CheckResponse(reportFileURL, null);
		return sendBody(STATUS_OK, body);
	}

	private APIGatewayProxyResponseEvent sendError(int statusCode, Exception exception) {
		return sendError(statusCode, exception.toString());
	}

	private APIGatewayProxyResponseEvent sendError(int statusCode, String errorMessage) {
		var body = new CheckResponse(null, errorMessage);
		return sendBody(statusCode, body);
	}

	private APIGatewayProxyResponseEvent sendNotFound() {
		// return "404 - not found" error response
		return new APIGatewayProxyResponseEvent()
				.withStatusCode(STATUS_NOT_FOUND);
	}

	private APIGatewayProxyResponseEvent sendBody(int statusCode, Object body) {

		// prepare headers
		var headers = new LinkedHashMap<String, String>();
		headers.put("Content-Type", "application/json");

		// serialize body to JSON
		var json = gson.toJson(body);

		// return API response
		return new APIGatewayProxyResponseEvent()
				.withStatusCode(statusCode)
				.withHeaders(headers)
				.withBody(json);
	}

}