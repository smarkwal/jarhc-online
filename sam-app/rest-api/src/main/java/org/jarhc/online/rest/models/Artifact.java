package org.jarhc.online.rest.models;

public class Artifact {

	private final String groupId;
	private final String artifactId;
	private final String version;

	public Artifact(String coordinates) {

		if (!isValidVersion(coordinates)) {
			String message = String.format("Invalid artifact coordinates: '%s'", coordinates);
			throw new IllegalArgumentException(message);
		}

		// split coordinates (must have 3 parts!)
		String[] parts = coordinates.split(":", 3);

		this.groupId = parts[0];
		this.artifactId = parts[1];
		this.version = parts[2];
	}

	public String getGroupId() {
		return groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public String getVersion() {
		return version;
	}

	public boolean exists() {
		// TODO: implement
		return false;
	}

	public static boolean isValidVersion(String version) {
		if (version == null || version.length() < 5) {
			return false;
		}
		return version.matches("^[^:]+:[^:]+:[^:]*[^.]$");
	}

}
