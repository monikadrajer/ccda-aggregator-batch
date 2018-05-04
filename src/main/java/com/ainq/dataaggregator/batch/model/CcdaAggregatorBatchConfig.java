package com.ainq.dataaggregator.batch.model;

public class CcdaAggregatorBatchConfig {

	private String ccdaFolderLocation;
	private String ccdaAggregatorUrl;
	private String outputFolder;

	public String getCcdaFolderLocation() {
		return ccdaFolderLocation;
	}

	public void setCcdaFolderLocation(String ccdaFolderLocation) {
		this.ccdaFolderLocation = ccdaFolderLocation;
	}

	public String getCcdaAggregatorUrl() {
		return ccdaAggregatorUrl;
	}

	public void setCcdaAggregatorUrl(String ccdaAggregatorUrl) {
		this.ccdaAggregatorUrl = ccdaAggregatorUrl;
	}

	public String getOutputFolder() {
		return outputFolder;
	}

	public void setOutputFolder(String outputFolder) {
		this.outputFolder = outputFolder;
	}

}
