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

    @HystrixCommand(fallbackMethod = "service1FallbackMethod")
    public String service1OKMethod() throws InterruptedException {
        return restTemplate.getForObject("http://service2/fallbacktest",String.class);
    }

    public String service1FallbackMethod() throws InterruptedException{
        return "service1_Fail_method called";
    }
}