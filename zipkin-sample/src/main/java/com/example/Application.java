package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	@Bean
	@LoadBalanced
	RestTemplate restTemplate(){ return new RestTemplate();}

	@Autowired
	RestTemplate restTemplate;

	@RequestMapping("/")
	public String method1() throws InterruptedException {
		return "OK";
	}

	@RequestMapping("/call")
	public String call() throws InterruptedException {
		return restTemplate.getForObject("http://service2/call",String.class);
	}

	@RequestMapping("/call-timeout")
	public String callTimeout() throws InterruptedException {
		Thread.sleep(1000);
		return "timeout";
	}

}


