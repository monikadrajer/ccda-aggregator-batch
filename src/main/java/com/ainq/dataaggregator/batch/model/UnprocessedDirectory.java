package com.ainq.dataaggregator.batch.model;

public class UnprocessedDirectory {

	private String directoryName;
	private String reason;

	public String getDirectoryName() {
		return directoryName;
	}

	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	public String toString() {
		return directoryName + ":" + reason;
	}
}
