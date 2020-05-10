package com.itc.com.itc.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ResourceController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(method = RequestMethod.GET,value = "/test")
    public ResponseEntity<?> testMethd(){
        String successMsg = "Successfully login into client application.......";
        return new ResponseEntity<String>(successMsg, HttpStatus.OK);
    }


    @HystrixCommand(fallbackMethod = "fallback", groupKey = "Hello",
            commandKey = "hello",
            threadPoolKey = "helloThread"
    )
    @RequestMapping(method = RequestMethod.GET,value = "/hello")
    public String hello() {
        String url = "http://server-app/hello";
        return restTemplate.getForObject(url, String.class)+ " including client";
    }

    public String fallback(Throwable hystrixCommand) {
        return "Fall Back Hello world";
    }

}
