package maven

import (
	"fmt"
	"io"
	"log"
	"net/http"
	"net/url"
	"os"
	"path"
	"strings"
)

// base URL for Maven Central repository
const repoBaseURL = "https://repo1.maven.org/maven2"

// cache In-memory cache to remember existing/non-existing artifacts.
// key: Artifact coordinates
// value: true = found, false = not found
var cache = make(map[string]bool)

type Artifact struct {
	GroupId    string `json:"groupId"`
	ArtifactId string `json:"artifactId"`
	Version    string `json:"version"`
}

// NewArtifact Create a new Artifact given its coordinates.
func NewArtifact(coordinates string) (*Artifact, error) {

	// split coordinates (must have 3 parts!)
	parts := strings.Split(coordinates, ":")
	if len(parts) != 3 {
		err := fmt.Errorf("invalid artifact coordinates: '%s'", coordinates)
		return nil, err
	}

	// create new Artifact struct
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

// Download Download artifact to given file path.
func (artifact *Artifact) Download(filePath string) error {

	// get artifact URL for given coordinates
	artifactURL := artifact.GetURL()
	log.Println("artifact URL:", artifactURL)

	// download artifact
	resp, err := http.Get(artifactURL)
	if err != nil {
		return err
	}
	//goland:noinspection ALL
	defer resp.Body.Close()

	// check status code
	statusCode := resp.StatusCode
	log.Println("status code:", statusCode)
	if statusCode != 200 {
		err := fmt.Errorf("unexpected status code: %d", statusCode)
		return err
	}

	// create file
	out, err := os.Create(filePath)
	if err != nil {
		return err
	}
	//goland:noinspection ALL
	defer out.Close()

	// copy response body to file
	_, err = io.Copy(out, resp.Body)
	return err
}

// GetURL Generate artifact URL based on coordinates.
func (artifact *Artifact) GetURL() string {

	var artifactURL strings.Builder
	artifactURL.WriteString(repoBaseURL)
	for _, part := range strings.Split(artifact.GroupId, ".") {
		artifactURL.WriteString("/")
		artifactURL.WriteString(url.PathEscape(part))
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

// ToPath Generate artifact path based on coordinates.
func (artifact *Artifact) ToPath() string {
	artifactPath := ""
	for _, part := range strings.Split(artifact.GroupId, ".") {
		artifactPath = path.Join(artifactPath, part)
	}
	artifactPath = path.Join(artifactPath, artifact.ArtifactId)
	artifactPath = path.Join(artifactPath, artifact.Version)
	artifactPath = path.Join(artifactPath, artifact.ArtifactId+"-"+artifact.Version+".jar")
	return artifactPath
}

func (artifact *Artifact) ToCoordinates() string {
	return fmt.Sprintf("%s:%s:%s", artifact.GroupId, artifact.ArtifactId, artifact.Version)
}
