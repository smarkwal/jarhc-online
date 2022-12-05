package org.jarhc.online.jarhc;

import java.util.List;

public class JarhcCheckRequest {

	private List<String> classpath;
	private List<String> provided;
	private String reportFileName;

	public JarhcCheckRequest() {
	}

	public List<String> getClasspath() {
		return classpath;
	}

	public void setClasspath(List<String> classpath) {
		this.classpath = classpath;
	}

	public List<String> getProvided() {
		return provided;
	}

	public void setProvided(List<String> provided) {
		this.provided = provided;
	}

	public String getReportFileName() {
		return reportFileName;
	}

	public void setReportFileName(String reportFileName) {
		this.reportFileName = reportFileName;
	}

}
