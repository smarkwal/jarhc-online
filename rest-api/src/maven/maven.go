package maven

import (
	"fmt"
	"net/http"
	"net/url"
	"strings"
)

const repoBaseURL = "https://repo1.maven.org/maven2"

type Artifact struct {
	GroupId    string `json:"groupId"`
	ArtifactId string `json:"artifactId"`
	Version    string `json:"version"`
}

func NewArtifact(coordinates string) (*Artifact, error) {

	parts := strings.Split(coordinates, ":")
	if len(parts) != 3 {
		err := fmt.Errorf("invalid artifact coordinates: '%s'", coordinates)
		return nil, err
	}

	artifact := Artifact{
		GroupId:    parts[0],
		ArtifactId: parts[1],
		Version:    parts[2],
	}

	return &artifact, nil
}

// Exists Check if the artifact exists in Maven Central.
func (artifact *Artifact) Exists() (bool, error) {

	// get artifact URL for given coordinates
	artifactURL := artifact.GetURL()

	// check if artifact URL is valid
	resp, err := http.Head(artifactURL)
	if err != nil {
		return false, err
	}

	// return result based on status code
	statusCode := resp.StatusCode
	if statusCode == http.StatusOK {
		return true, nil
	} else if statusCode == http.StatusNotFound {
		return false, nil
	} else {
		err := fmt.Errorf("unexpected status code: %d", statusCode)
		return false, err
	}
}

// GetURL Generate artifact URL based on coordinates.
func (artifact *Artifact) GetURL() string {

	artifactURL := fmt.Sprintf(
		"%s/%s/%s/%s/%[3]s-%[4]s.jar",
		repoBaseURL,
		url.PathEscape(artifact.GroupId),
		url.PathEscape(artifact.ArtifactId),
		url.PathEscape(artifact.Version),
	)

	return artifactURL
}
