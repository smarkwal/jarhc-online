package org.jarhc.online.japicc;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Subsegment;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class JAPICC {

	private static final Logger logger = LoggerFactory.getLogger(JAPICC.class);

	private static final String JAPI_COMPLIANCE_CHECKER = "/usr/bin/japi-compliance-checker";

	void execute(File oldVersionFile, File newVersionFile, File reportFile) {
		try (Subsegment xray = AWSXRay.beginSubsegment("JAPICC.execute")) {

			int exitCode;
			try {

				// run JAPICC
				Process process = new ProcessBuilder()
						.command(
								JAPI_COMPLIANCE_CHECKER,
								"-old", oldVersionFile.getAbsolutePath(),
								"-new", newVersionFile.getAbsolutePath(),
								"-report-path", reportFile.getAbsolutePath()
						)
						.start();

				// wait for process to finish
				exitCode = process.waitFor();

				// read output from STDOUT
				try (InputStream inputStream = process.getInputStream()) {
					ByteArrayOutputStream buffer = new ByteArrayOutputStream();
					inputStream.transferTo(buffer);
					String output = buffer.toString();
					logger.info("JAPICC output:\n{}", output);
				}

				// read output from STDERR
				try (InputStream errorStream = process.getErrorStream()) {
					ByteArrayOutputStream buffer = new ByteArrayOutputStream();
					errorStream.transferTo(buffer);
					String output = buffer.toString();
					if (!output.isEmpty()) {
						logger.error("JAPICC error:\n{}", output);
					}
				}

			} catch (Exception e) {
				xray.addException(e);
				throw new RuntimeException("Error running JAPICC.", e);
			}

			// handle exit code
			switch (exitCode) {
				case 0: // OK (no incompatibilities)
				case 1: // OK (incompatibilities found)
					break;
				default:
					throw new RuntimeException("JAPICC failed with exit code " + exitCode + ".");
			}

		}
	}
}
