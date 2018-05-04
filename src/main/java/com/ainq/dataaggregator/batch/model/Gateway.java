package com.ainq.dataaggregator.batch.model;

import java.util.ArrayList;
import java.util.List;

public class Gateway{
		
		private String uri;
		
		private String name;
		
		private List<String> xdsMetadata = new ArrayList<String>();
		
		private List<String> ccdas = new ArrayList<String>();
		
		private List<String> documentUuid = new ArrayList<String>();

		public String getUri() {
			return uri;
		}

		public void setUri(String uri) {
			this.uri = uri;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<String> getXdsMetadata() {
			return xdsMetadata;
		}

		public void setXdsMetadata(List<String> xdsMetadata) {
			this.xdsMetadata = xdsMetadata;
		}

		public List<String> getCcdas() {
			return ccdas;
		}

		public void setCcdas(List<String> ccdas) {
			this.ccdas = ccdas;
		}

		public List<String> getDocumentUuid() {
			return documentUuid;
		}

		public void setDocumentUuid(List<String> documentUuid) {
			this.documentUuid = documentUuid;
		}
		
}
