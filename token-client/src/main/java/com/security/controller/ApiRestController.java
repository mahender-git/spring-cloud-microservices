package com.security.controller;


import com.security.response.JwtAuthenticationResponse;
import com.security.service.CustomUserDetailsService;
import com.security.utils.JwtUtil;
import com.security.vo.ResponseObject;
import com.security.vo.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class ApiRestController {

    private static final Logger logger = LoggerFactory.getLogger(ApiRestController.class);

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @RequestMapping(value = "/test", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<ResponseObject> testApi(){
        ResponseObject obj = new ResponseObject("success","successfully entered into controller........",null);
        return  new ResponseEntity(obj,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/userInfo/{username}", method = RequestMethod.POST,produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<ResponseObject> userInfo(@RequestBody UserInfo userInfo, @PathVariable String username){
        ResponseObject obj = new ResponseObject("success","successfully entered into controller........",userInfo);
        return  new ResponseEntity(obj,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/secured/all")
    public String securedHello() {
        System.out.println("entered into securedHello");
        return "Secured Hello";
    }


    @RequestMapping(value = "/generateToken", method = RequestMethod.POST)
    public ResponseEntity<?> genarateToken(@RequestBody UserInfo userInfo) throws AuthenticationException {
        logger.info("this is company login , and active info is : ");
        // Perform the security
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userInfo.getUsername(),
                            userInfo.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            logger.error("AuthenticationException::{}",e);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(userInfo.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        logger.info("succesfully retured token::{}",token);
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }

}
