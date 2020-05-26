package com.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.security"})
public class TokenClientApplication {
	private static final Logger logger = LoggerFactory.getLogger(TokenClientApplication.class);


	public static void main(String[] args) {
		SpringApplication.run(TokenClientApplication.class, args);
		logger.info("Token client applicaion started ............");
	}

}
