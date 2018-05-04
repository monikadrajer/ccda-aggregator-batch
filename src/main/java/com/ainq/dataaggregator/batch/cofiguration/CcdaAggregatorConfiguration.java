package com.ainq.dataaggregator.batch.cofiguration;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.ainq.dataaggregator.batch.model.CcdaAggregatorBatchConfig;
import com.ainq.dataaggregator.batch.util.ApplicationUtil;


@Configuration
@PropertySource("file:${configFileLocation}")
public class CcdaAggregatorConfiguration {
	
	@Autowired
	Environment environment;
	
	public SimpleClientHttpRequestFactory getRequestFactory(Integer timeOutInMillSecs) {
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
    	factory.setReadTimeout(timeOutInMillSecs);
    	factory.setConnectTimeout(timeOutInMillSecs);
		return factory;
	}
	
	@Bean(name="restTemplate")
	public RestTemplate getRestTemplate(@Value("${api.rest.timeout.ms}") Integer timeOutInMillSecs) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(getRequestFactory(timeOutInMillSecs));
		return restTemplate;
	}
	
	@Bean(name="ccdaAggregatorBatchConfig")
	public CcdaAggregatorBatchConfig getScorecardBatchConfig(final Environment environment)
	{
		CcdaAggregatorBatchConfig ccdaAggregatorBatchConfig = new CcdaAggregatorBatchConfig();
		ccdaAggregatorBatchConfig.setCcdaFolderLocation(environment.getProperty("ccdaaggregator.ccdaFolderLocation"));
		ccdaAggregatorBatchConfig.setCcdaAggregatorUrl(environment.getProperty("ccdaaggregator.ccdaAggregatorUrl"));
		ccdaAggregatorBatchConfig.setOutputFolder(checkTrailngSlashForOutputFolder(environment.getProperty("ccdaaggregator.outputFolderLocation")));
		return ccdaAggregatorBatchConfig;
	}
	
	private String checkTrailngSlashForOutputFolder(String outputFolder)
	{
		if(!ApplicationUtil.isEmpty(outputFolder))
		{
			if(outputFolder.charAt(outputFolder.length()-1)!=File.separatorChar){
				outputFolder += File.separator;
			}
		}
		return outputFolder;
	}
}
