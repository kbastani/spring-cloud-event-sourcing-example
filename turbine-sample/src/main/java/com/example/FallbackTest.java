package com.example;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class FallbackTest {

    @Bean
    @LoadBalanced
    RestTemplate restTemplate(){ return new RestTemplate();}

    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "service1_Fail_method")
    public String service1_OK_method() throws InterruptedException {
        return "ok";
    }

    public String service1_Fail_method() throws InterruptedException{
        return "service1_Fail_method called";
    }
}