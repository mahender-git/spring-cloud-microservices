package com.itc.com.itc.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceController {
    @RequestMapping(method = RequestMethod.GET,value = "/hello")
    public ResponseEntity<?> testMethd(){
        String successMsg = "Successfully login into server application.......";
        return new ResponseEntity<String>(successMsg,HttpStatus.OK);
    }
}
