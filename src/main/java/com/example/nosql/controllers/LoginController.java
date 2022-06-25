package com.example.nosql.controllers;

import com.example.nosqldb.AdminManager;
import com.example.nosqldb.LoadBalance;
import com.example.nosqldb.schema.UsersDB;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private Logger logger = LogManager.getLogger(CRUDControllers.class);
    @Autowired
    private static LoadBalance loadBalance;
    @Autowired
    private static AdminManager adminManager;

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


        return "index";
    }

    @PostMapping("/connect")
    public String connect(@RequestBody UsersDB user) {

        //Authentication auth = getAuth();

        //String username = authName(auth);
        logger.info("in connection control");
        String result = adminManager.connect(user);
        return null;
    }

    @GetMapping("/disconnect")
    public String disconnect() {
        return "logout";
    }
}
