package com.example.nosql.controllers;

import com.example.nosql.auth.AdminManager;
import com.example.nosql.LoadBalance;
import com.example.nosql.schema.UsersDB;
import com.example.nosql.schema.UsersDBToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class LoginController {

    private Logger logger = LogManager.getLogger(LoginController.class);
    @Autowired
    private LoadBalance loadBalance;
    @Autowired
    private AdminManager adminManager;

    /*protected String authName(Authentication auth) {

        //SecurityContext context = SecurityContextHolder.getContext();
        //Authentication authentication = context.getAuthentication();

        String userAuth = auth.getName();
        Object principal = auth.getPrincipal();

        return userAuth;
    }

    protected Authentication getAuth() {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        return authentication;
    }*/

 //   @Autowired
 //   private AuthenticationManager authenticationManager;


    @GetMapping("/default-user")
    public String defaultuser() {
        logger.info("in default user");

        return "index";
    }

    @PostMapping("/connect")
    //public String connect(@RequestBody UsersDB user) {
    public ResponseEntity<Void> connect(@RequestBody UsersDB user) {

        //Authentication auth = getAuth();

        //String username = authName(auth);
        logger.info("in connection control");
        String result = adminManager.connect(user);
        ResponseEntity<Void> responseEntity = null;

        if (!result.equals("false")) {
            responseEntity = ResponseEntity.status(HttpStatus.FOUND).location(URI.create(result)).build();
        }
        return responseEntity;
    }

    @PostMapping("/registration")
    public String register(@RequestBody UsersDB user) {

        logger.info("register new User");
        String usersDBToken = adminManager.register(user);
        return usersDBToken;
    }

    @GetMapping("/disconnect")
    public String disconnect() {
        return "logout";
    }
}
