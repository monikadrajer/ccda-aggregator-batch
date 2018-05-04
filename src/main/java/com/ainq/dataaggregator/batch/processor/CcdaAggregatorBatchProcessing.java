package com.ainq.dataaggregator.batch.processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.ainq.dataaggregator.batch.model.CcdaAggregatorBatchConfig;
import com.ainq.dataaggregator.batch.model.Gateway;
import com.ainq.dataaggregator.batch.model.QueryInformation;
import com.ainq.dataaggregator.batch.model.UnprocessedDirectory;
import com.ainq.dataaggregator.batch.util.ApplicationConstants.CcdaAggregatorBatchDefaults;
import com.ainq.dataaggregator.batch.util.ApplicationUtil;

@Service
public class CcdaAggregatorBatchProcessing {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	@Qualifier("restTemplate")
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("ccdaAggregatorBatchConfig")
	private CcdaAggregatorBatchConfig ccdaAggregatorBatchConfig;

	@PostConstruct
	public void processCCdaFiles() throws IOException {

		String ccdaAggregatorUrl;
		List<UnprocessedDirectory> unprocessedDirectoryList = new ArrayList<UnprocessedDirectory>();
		UnprocessedDirectory unprocessedDirectory = null;
		FileOutputStream out = null;
		QueryInformation queryInfo = new QueryInformation();
		Gateway gateway= null;
		List<Gateway> gatewayList = null;
		
		try {
			File directory = loadDirectory(ccdaAggregatorBatchConfig.getCcdaFolderLocation());
			File[] directoryList = directory.listFiles();

			if (directoryList == null || directoryList.length == 0) {
				logger.info("No files/folders to process in given CCDAFileLocation : "
						+ ccdaAggregatorBatchConfig.getCcdaFolderLocation());
				return;
			}

			for (File ccdaDirectory : directoryList) {

				logger.info("Processing Folder : " + ccdaDirectory.getName());
				
				queryInfo.setQueryId(ccdaDirectory.getName());
				File[] fileList = ccdaDirectory.listFiles();

				if (fileList != null && fileList.length > 0) {
					gatewayList = new ArrayList<>();
					gateway = new Gateway();
					List<String> ccdaFiles= new ArrayList<>();
					
					for (File ccdaFile : fileList) {
						ccdaFiles.add(convertXMlToString(ccdaFile));
					}
					gateway.setCcdas(ccdaFiles);
					gatewayList.add(gateway);
				}
				queryInfo.setGateways(gatewayList);
				HttpHeaders headers = new HttpHeaders();
				headers.add("Accept", "application/json");
				headers.add("Content-Type", "application/json");
				HttpEntity<QueryInformation> request = new HttpEntity<>(queryInfo,headers);

				try {
					ccdaAggregatorUrl = getCcdaAggregatorUrl(ccdaAggregatorBatchConfig);
					restTemplate.postForEntity(ccdaAggregatorUrl, request, Void.class);

				} catch (RestClientException exception) {
					logger.error(exception.getMessage(), exception);
					unprocessedDirectory = new UnprocessedDirectory();
					unprocessedDirectory.setDirectoryName(ccdaDirectory.getName());
					unprocessedDirectory.setReason(exception.getMessage());
					unprocessedDirectoryList.add(unprocessedDirectory);
				} catch (Exception exce) {
					logger.error(exce.getMessage(), exce);
					unprocessedDirectory = new UnprocessedDirectory();
					unprocessedDirectory.setDirectoryName(ccdaDirectory.getName());
					unprocessedDirectory.setReason(exce.getMessage());
					unprocessedDirectoryList.add(unprocessedDirectory);
				}
			}
			if (!ApplicationUtil.isEmpty(unprocessedDirectoryList)) {
				FileUtils.writeStringToFile(
						new File(ccdaAggregatorBatchConfig.getOutputFolder()
								.concat(CcdaAggregatorBatchDefaults.errorFileName)),
						generateErrorReport(unprocessedDirectoryList));
			}
			logger.info("************CCDA aggregator Batch Execution Completed***********************");
		} catch (IOException ioe) {
			logger.error(ioe.getLocalizedMessage(), ioe);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	private String convertXMlToString(File xmlFile) throws IOException {
		Reader fileReader = new FileReader(xmlFile);
		BufferedReader bufReader = new BufferedReader(fileReader);
		StringBuilder sb = new StringBuilder();
		String line = bufReader.readLine();
		while (line != null) {
			sb.append(line).append("\n");
			line = bufReader.readLine();
		}
		if(bufReader!=null){
			bufReader.close();
		}
		return sb.toString();
	}

	private String generateErrorReport(List<UnprocessedDirectory> unprocessedFiles) {
		StringBuffer errorMeessage = new StringBuffer();
		for (UnprocessedDirectory unprocessedFile : unprocessedFiles) {
			errorMeessage.append(unprocessedFile.toString());
			errorMeessage.append(System.lineSeparator());
		}
		return errorMeessage.toString();
	}

	private File loadDirectory(String directory) throws IOException {
		File dir = new File(directory);
		if (dir.isFile()) {
			throw new IOException("CCDAFiles directoty location is invalid");
		}
		return dir;
	}

	private String getCcdaAggregatorUrl(CcdaAggregatorBatchConfig scorecardBatchConfig) {
		if (ApplicationUtil.isEmpty(scorecardBatchConfig.getCcdaAggregatorUrl())) {
			return CcdaAggregatorBatchDefaults.ccdaAggregatorDefaultUrl;
		} else
			return scorecardBatchConfig.getCcdaAggregatorUrl();
	}
}
