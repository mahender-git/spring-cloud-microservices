package com.itc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class EurekaServerRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaServerRegistryApplication.class, args);
	}

}
