package com.itc.com.itc.rest;

import com.itc.com.itc.vo.ResponseObject;
import com.itc.com.itc.vo.UserInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ResourceController {
    @RequestMapping(method = RequestMethod.GET,value = "/hello")
    public ResponseEntity<?> testMethd(){
        String successMsg = "Successfully login into server application.......";
        return new ResponseEntity<String>(successMsg,HttpStatus.OK);
    }

    @RequestMapping(value = "/userInfo/{username}", method = RequestMethod.POST,produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<ResponseObject> userInfo(@RequestBody UserInfo userInfo, @PathVariable String username){
        ResponseObject obj = new ResponseObject("success","successfully entered into controller........",userInfo);
        return  new ResponseEntity(obj,HttpStatus.OK);
    }

}
