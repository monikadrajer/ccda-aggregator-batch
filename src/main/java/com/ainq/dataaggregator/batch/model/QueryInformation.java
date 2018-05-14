package com.ainq.dataaggregator.batch.model;

import java.util.ArrayList;
import java.util.List;

public class QueryInformation {
	
	private String queryId;
	
	private List<Gateway> gateways = new ArrayList<Gateway>();
	
	public String getQueryId() {
		return queryId;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	public List<Gateway> getGateways() {
		return gateways;
	}

	public void setGateways(List<Gateway> gateways) {
		this.gateways = gateways;
	}

}
