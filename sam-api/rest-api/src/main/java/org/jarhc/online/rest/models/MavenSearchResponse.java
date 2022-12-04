package org.jarhc.online.rest.models;

import java.util.List;

public class MavenSearchResponse {

	private final String coordinates;
	private final List<Artifact> artifacts;

	public MavenSearchResponse(String coordinates, List<Artifact> artifacts) {
		this.coordinates = coordinates;
		this.artifacts = artifacts;
	}

	public String getCoordinates() {
		return coordinates;
	}

	public List<Artifact> getArtifacts() {
		return artifacts;
	}

}
