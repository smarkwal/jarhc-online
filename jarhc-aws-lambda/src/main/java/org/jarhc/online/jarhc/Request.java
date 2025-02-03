package org.jarhc.online.jarhc;

import java.util.List;

public class Request {

	private List<String> classpath = List.of();
	private List<String> provided = List.of();
	private String reportFileName;

	public Request() {
	}

	public List<String> getClasspath() {
		return classpath;
	}

	public void setClasspath(List<String> classpath) {
		if (classpath == null) {
			classpath = List.of();
		}
		this.classpath = classpath;
	}

	public List<String> getProvided() {
		return provided;
	}

	public void setProvided(List<String> provided) {
		if (provided == null) {
			provided = List.of();
		}
		this.provided = provided;
	}

	public String getReportFileName() {
		return reportFileName;
	}

	public void setReportFileName(String reportFileName) {
		this.reportFileName = reportFileName;
	}

}
