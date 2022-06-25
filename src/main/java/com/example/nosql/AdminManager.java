package com.example.nosql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdminManager  {

    private static Logger logger = LogManager.getLogger(AdminManager.class);

    @Autowired
    public AdminManager(){}



    /*ublic String connect(UsersDB userdb) {

        String result = null;
        int uuid = userdb.getUuid();
        UsersDB user = SharedClass.fromJsonUser(uuid);
        if (user.getUuid() == userdb.getUuid()) {
            result = "true";
            String database = userdb.getDatabase();

        }
        else
            result = "false";



        return null;

    }*/
}
