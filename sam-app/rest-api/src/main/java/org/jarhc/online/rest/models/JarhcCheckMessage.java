package org.jarhc.online.rest.models;

import java.util.List;

public class JarhcCheckMessage {

	private final List<String> classpath;
	private final List<String> provided;
	private final String reportFileName;

	public JarhcCheckMessage(List<String> classpath, List<String> provided, String reportFileName) {
		this.classpath = classpath;
		this.provided = provided;
		this.reportFileName = reportFileName;
	}

	public List<String> getClasspath() {
		return classpath;
	}

	public List<String> getProvided() {
		return provided;
	}

	public String getReportFileName() {
		return reportFileName;
	}

}
