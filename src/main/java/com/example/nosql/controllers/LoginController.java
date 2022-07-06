package com.example.nosql.controllers;

import com.example.nosql.auth.AdminManager;
import com.example.nosql.LoadBalance;
import com.example.nosql.auth.JWTUtil;
import com.example.nosql.schema.UsersDB;
import com.example.nosql.schema.UsersDBToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTUtil jwtUtil;

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

    @PostMapping("/auth/connect")
    //public String connect(@RequestBody UsersDB user) {
    public ResponseEntity<?> connect(@RequestBody UsersDB user) {

        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
            UsersDB usersDB = (UsersDB) authentication.getPrincipal();
        //    String accessToken = "Try to JWT token";
            String accessToken = jwtUtil.generateToken(usersDB);
            UsersDBToken usersDBToken = new UsersDBToken(usersDB.getUsername(),accessToken);

            return ResponseEntity.ok(usersDBToken);

        }catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        /*String result = adminManager.connect(user);
        logger.info("result from checking connection by adminManager:"+result);
        ResponseEntity<Void> responseEntity = null;

        if (!result.equals("false")) {
            //this is for forwarding to an instance node database
            responseEntity = ResponseEntity.status(HttpStatus.FOUND).location(URI.create(result)).build();
        }
        user.setDatabase(result);

        //return result;
        return user;*/
    }

    @PostMapping("/registration")
    public UsersDB register(@RequestBody UsersDB user) {

        logger.info("register new User");
        //String usersDBToken = adminManager.register(user);
        UsersDB usersDB = adminManager.register(user);
        return usersDB;
    }

    @GetMapping("/disconnect")
    public String disconnect() {
        return "logout";
    }
}
