package com.ainq.dataaggregator.batch.processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.ainq.dataaggregator.batch")
public class CcdaAggregatorBatchProcessor {
	
	public static void main(String[] args) {
		SpringApplication.run(CcdaAggregatorBatchProcessor.class, args);
	}
}
