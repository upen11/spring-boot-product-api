package com.securityexample.securityex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SecurityexApplication {

	private static final Logger logger = LoggerFactory.getLogger(SecurityexApplication.class);

	public static void main(String[] args) {
		logger.info("*** Starting SecurityexApplication... ***");
		SpringApplication.run(SecurityexApplication.class, args);
		logger.info("*** SecurityexApplication started successfully. ***");
	}
}
