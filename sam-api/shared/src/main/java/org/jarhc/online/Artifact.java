package org.jarhc.online;

import static org.jarhc.online.Utils.encodeURL;

import java.util.Objects;

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

	public String toURLPath() {
		StringBuilder path = new StringBuilder();
		for (String part : groupId.split("\\.")) {
			path.append('/').append(encodeURL(part));
		}
		path.append('/');
		path.append(encodeURL(artifactId));
		path.append('/');
		path.append(encodeURL(version));
		path.append('/');
		path.append(encodeURL(artifactId));
		path.append('-');
		path.append(encodeURL(version));
		path.append(".jar");
		return path.toString();
	}

	/**
	 * Generates artifact path based on coordinates.
	 */
	public String toFilePath() {
		return String.format("%s/%s/%s/%s-%s.jar", groupId.replace('.', '/'), artifactId, version, artifactId, version);
	}

	public String toCoordinates() {
		return String.format("%s:%s:%s", groupId, artifactId, version);
	}

	public static boolean isValidVersion(String version) {
		if (version == null || version.length() < 5) {
			return false;
		}
		return version.matches("^[^:]+:[^:]+:[^:]*[^.]$");
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Artifact artifact = (Artifact) obj;
		return groupId.equals(artifact.groupId) && artifactId.equals(artifact.artifactId) && version.equals(artifact.version);
	}

	@Override
	public int hashCode() {
		return Objects.hash(groupId, artifactId, version);
	}
}
