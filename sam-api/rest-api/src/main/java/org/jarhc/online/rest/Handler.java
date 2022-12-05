package org.jarhc.online.rest;

import static org.jarhc.online.Artifact.isValidVersion;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Subsegment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jarhc.online.Artifact;
import org.jarhc.online.JsonUtils;
import org.jarhc.online.Utils;
import org.jarhc.online.clients.Lambda;
import org.jarhc.online.clients.Maven;
import org.jarhc.online.clients.S3;
import org.jarhc.online.rest.models.JapiccCheckMessage;
import org.jarhc.online.rest.models.JapiccCheckRequest;
import org.jarhc.online.rest.models.JarhcCheckMessage;
import org.jarhc.online.rest.models.JarhcCheckRequest;
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
	private final Maven maven;

	public Handler() {
		logger.debug("Initializing Handler ...");

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
		maven = new Maven(10 * 1000); // 10 seconds

		logger.debug("Handler initialized.");
	}

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {

		if (logger.isTraceEnabled()) {
			logger.trace("Environment:\n{}", JsonUtils.toJSON(System.getenv()));
			logger.trace("Request:\n{}", JsonUtils.toJSON(request));
			logger.trace("Context:\n{}", JsonUtils.toJSON(context));
		}

		// get current user
		var user = getUser(request);
		logger.info("User: {}", user);

		// dispatch request to correct method based on path
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
		if (context != null) {

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
		}

		return new User(subject, email, sourceIP, userAgent);
	}

	private APIGatewayProxyResponseEvent dispatch(APIGatewayProxyRequestEvent request) {

		// get request URL path
		var path = request.getPath();
		logger.info("Path: {}", path);
		if (path == null) {
			return sendError(STATUS_BAD_REQUEST, "Missing path");
		}

		try (Subsegment xray = AWSXRay.beginSubsegment(path)) {

			// dispatch request to correct handler
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
					// return "404 - not found" error response
					return sendError(STATUS_NOT_FOUND, "Not found: " + path);
			}

		}

	}

	private APIGatewayProxyResponseEvent handleMavenSearch(APIGatewayProxyRequestEvent request) {

		// get message from request body
		var message = request.getBody();
		logger.debug("Message: {}", message);

		// parse JSON message
		MavenSearchRequest in;
		try {
			in = JsonUtils.fromJSON(message, MavenSearchRequest.class);
		} catch (Exception e) {
			logger.debug("Error parsing JSON message:", e);
			return sendError(STATUS_BAD_REQUEST, e); // TODO: throw exception instead of returning response?
		}

		// get coordinates from message
		var coordinates = in.getCoordinates();
		if (coordinates == null) {
			return sendError(STATUS_BAD_REQUEST, "Missing parameter: 'coordinates'");
		}

		// parse coordinates
		Artifact artifact;
		try {
			artifact = new Artifact(coordinates);
		} catch (Exception e) {
			return sendError(STATUS_BAD_REQUEST, e);
		}

		// check if artifact exists
		boolean exists;
		try {
			exists = maven.exists(artifact);
		} catch (Exception e) {
			return sendError(STATUS_INTERNAL_SERVER_ERROR, e);
		}

		// prepare list of artifacts
		var artifacts = new ArrayList<Artifact>();
		if (exists) {
			artifacts.add(artifact);
		}

		// prepare response body
		var body = new MavenSearchResponse(coordinates, artifacts);

		return sendResponse(STATUS_OK, body);
	}

	private APIGatewayProxyResponseEvent handleJapiccSubmit(APIGatewayProxyRequestEvent request) {

		// get message from request body
		var message = request.getBody();
		logger.debug("Message: {}", message);

		// parse JSON message
		JapiccCheckRequest in;
		try {
			in = JsonUtils.fromJSON(message, JapiccCheckRequest.class);
		} catch (Exception e) {
			logger.error("Error parsing JSON message:", e);
			return sendError(STATUS_BAD_REQUEST, e);
		}

		var oldVersion = in.getOldVersion();
		logger.info("Old version: {}", oldVersion);

		// check if old version is valid
		if (!isValidVersion(oldVersion)) {
			logger.error("Version is not valid: {}", oldVersion);
			return sendError(STATUS_BAD_REQUEST, "Version is not valid: " + oldVersion);
		}

		var newVersion = in.getNewVersion();
		logger.info("New version: {}", newVersion);

		// check if new version is valid
		if (!isValidVersion(newVersion)) {
			logger.error("Version is not valid: {}", newVersion);
			return sendError(STATUS_BAD_REQUEST, "Version is not valid: " + newVersion);
		}

		if (oldVersion.equals(newVersion)) {
			logger.error("Submitted same version twice: {}", oldVersion);
			return sendError(STATUS_BAD_REQUEST, "Cannot compare version with itself: " + oldVersion);
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
		try {
			var artifact = new Artifact(oldVersion);
			exists = maven.exists(artifact);
		} catch (Exception e) {
			return sendError(STATUS_INTERNAL_SERVER_ERROR, e);
		}
		if (!exists) {
			logger.error("Version not found in Maven Central: {}", oldVersion);
			return sendError(STATUS_BAD_REQUEST, "Version not found in Maven Central: " + oldVersion);
		}

		// check if new version exists in Maven Central
		try {
			var artifact = new Artifact(newVersion);
			exists = maven.exists(artifact);
		} catch (Exception e) {
			return sendError(STATUS_INTERNAL_SERVER_ERROR, e);
		}
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
			in = JsonUtils.fromJSON(message, JarhcCheckRequest.class);
		} catch (Exception e) {
			logger.error("Error parsing JSON message:", e);
			return sendError(STATUS_BAD_REQUEST, e);
		}

		// classpath is mandatory
		var classpath = in.getClasspath();
		if (classpath == null || classpath.isEmpty()) {
			return sendError(STATUS_BAD_REQUEST, "Missing parameter: 'classpath'");
		}

		for (var version : classpath) {
			logger.info("Classpath: {}", version);

			// check if version is valid
			if (!isValidVersion(version)) {
				logger.error("Version is not valid: {}", version);
				return sendError(STATUS_BAD_REQUEST, "Version is not valid: " + version);
			}

			// check if version exists in Maven Central
			boolean exists;
			try {
				var artifact = new Artifact(version);
				exists = maven.exists(artifact);
			} catch (Exception e) {
				return sendError(STATUS_INTERNAL_SERVER_ERROR, e);
			}
			if (!exists) {
				logger.error("Version not found in Maven Central: {}", version);
				return sendError(STATUS_BAD_REQUEST, "Version not found in Maven Central: " + version);
			}
		}

		// provided is optional
		var provided = in.getProvided();
		if (provided == null) {
			provided = Collections.emptyList();
		}

		for (var version : provided) {
			logger.info("Provided: {}", version);

			// check if version is valid
			if (!isValidVersion(version)) {
				logger.error("Version is not valid: {}", version);
				return sendError(STATUS_BAD_REQUEST, "Version is not valid: " + version);
			}

			// check if version exists in Maven Central
			boolean exists;
			try {
				var artifact = new Artifact(version);
				exists = maven.exists(artifact);
			} catch (Exception e) {
				return sendError(STATUS_INTERNAL_SERVER_ERROR, e);
			}
			if (!exists) {
				logger.error("Version not found in Maven Central: {}", version);
				return sendError(STATUS_BAD_REQUEST, "Version not found in Maven Central: " + version);
			}
		}

		// calculate hash for combination of versions
		var hash = Utils.sha256hex(String.join(";", classpath) + "/" + String.join(":", provided));

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

		var payload = new JarhcCheckMessage(classpath, provided, reportFileName);

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

		if (headers != null) {

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
		var body = Map.of("reportURL", reportFileURL);
		return sendResponse(STATUS_OK, body);
	}

	private APIGatewayProxyResponseEvent sendError(int statusCode, Exception exception) {
		return sendError(statusCode, exception.toString());
	}

	private APIGatewayProxyResponseEvent sendError(int statusCode, String errorMessage) {
		var body = Map.of("errorMessage", errorMessage);
		return sendResponse(statusCode, body);
	}

	private APIGatewayProxyResponseEvent sendResponse(int statusCode, Object body) {

		// prepare headers
		var headers = new LinkedHashMap<String, String>();
		headers.put("Content-Type", "application/json");

		// serialize body to JSON
		var json = JsonUtils.toJSON(body);

		// return API response
		return new APIGatewayProxyResponseEvent()
				.withStatusCode(statusCode)
				.withHeaders(headers)
				.withBody(json);
	}

}