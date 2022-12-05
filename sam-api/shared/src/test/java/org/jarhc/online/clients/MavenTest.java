package org.jarhc.online.clients;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import org.jarhc.online.Artifact;
import org.jarhc.online.clients.Maven;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;

@ExtendWith(AWSXRayExtension.class)
class MavenTest {

	private final Maven maven = new Maven(10 * 1000);

	@Test
	void exists() throws Exception {

		// prepare
		Artifact artifact = new Artifact("commons-lang:commons-lang:1.0");

		// test
		boolean result = maven.exists(artifact);

		// assert
		assertTrue(result);
	}

	@Test
	void exists_returnsFalse_ifArtifactIsNotFound() throws Exception {

		// prepare
		Artifact artifact = new Artifact("unknown:unknown:1.0.0");

		// test
		boolean result = maven.exists(artifact);

		// assert
		assertFalse(result);
	}

	@Test
	void download(@TempDir File tempDir) throws Exception {

		// prepare
		Artifact artifact = new Artifact("commons-lang:commons-lang:1.0");
		File file = new File(tempDir, artifact.toFilePath());

		// test
		boolean result = maven.download(artifact, file);

		// assert
		assertTrue(result);
		assertTrue(file.isFile());
		assertEquals(63861L, file.length());
	}

	@Test
	void download_returnsFalse_ifArtifactIsNotFound(@TempDir File tempDir) throws Exception {

		// prepare
		Artifact artifact = new Artifact("unknown:unknown:1.0.0");
		File file = new File(tempDir, artifact.toFilePath());

		// test
		boolean result = maven.download(artifact, file);

		// assert
		assertFalse(result);
		assertFalse(file.exists());
	}

}