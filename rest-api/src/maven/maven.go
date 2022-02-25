package maven

import (
	"fmt"
	"log"
	"net/http"
	"net/url"
	"strings"
)

const repoBaseURL = "https://repo1.maven.org/maven2"

var cache = make(map[string]bool)

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

	// check in-memory cache
	cacheKey := artifact.ToCoordinates()
	exists, cached := cache[cacheKey]
	if cached {
		if exists {
			log.Println("artifact found:", cacheKey, "(cached)")
			return true, nil
		} else {
			log.Println("artifact not found:", cacheKey, "(cached)")
			return false, nil
		}
	}

	// get artifact URL for given coordinates
	artifactURL := artifact.GetURL()
	log.Println("artifact URL:", artifactURL)

	// check if artifact URL is valid
	resp, err := http.Head(artifactURL)
	if err != nil {
		return false, err
	}

	// return result based on status code
	statusCode := resp.StatusCode
	if statusCode == http.StatusOK {
		log.Println("artifact found:", cacheKey)
		cache[cacheKey] = true
		return true, nil
	} else if statusCode == http.StatusNotFound {
		log.Println("artifact not found:", cacheKey)
		cache[cacheKey] = false
		return false, nil
	} else {
		err := fmt.Errorf("unexpected status code: %d", statusCode)
		return false, err
	}
}

// GetURL Generate artifact URL based on coordinates.
func (artifact *Artifact) GetURL() string {

	var artifactURL strings.Builder
	artifactURL.WriteString(repoBaseURL)
	for _, path := range strings.Split(artifact.GroupId, ".") {
		artifactURL.WriteString("/")
		artifactURL.WriteString(url.PathEscape(path))
	}
	artifactURL.WriteString("/")
	artifactURL.WriteString(url.PathEscape(artifact.ArtifactId))
	artifactURL.WriteString("/")
	artifactURL.WriteString(url.PathEscape(artifact.Version))
	artifactURL.WriteString("/")
	artifactURL.WriteString(url.PathEscape(artifact.ArtifactId))
	artifactURL.WriteString("-")
	artifactURL.WriteString(url.PathEscape(artifact.Version))
	artifactURL.WriteString(".jar")

	return artifactURL.String()
}

func (artifact *Artifact) ToCoordinates() string {
	return fmt.Sprintf("%s:%s:%s", artifact.GroupId, artifact.ArtifactId, artifact.Version)
}
