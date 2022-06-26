package com.example.nosql.auth;

import com.example.nosql.LoadBalance;
import com.example.nosql.MasterDB;
import com.example.nosql.schema.UsersDB;
import com.example.nosql.schema.UsersDBToken;
import com.example.nosql.shared.SharedClass;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

@Component
public class AdminManager  {

    private static Logger logger = LogManager.getLogger(AdminManager.class);
    //@Autowired
    //private InitialService service;
    @Autowired
    private LoadBalance loadBalance;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private MasterDB userDatabase;
    private SharedClass sharedClass = new SharedClass(restTemplate);
    @Autowired
    public AdminManager(){}


    public String connect(UsersDB userdb) {

        String result = null;
        String db = null;
        int uuid = userdb.getUuid();
        logger.info("checking for connection");
        logger.info(userdb.getUuid()+ " "+userdb.getUsername() + " in database:"+userDatabase.getDbName());
        UsersDB user = sharedClass.fromJsonUser(userdb.getUsername(),userDatabase);
        logger.info("checking with user in usersdb:"+user.getUsername());
        if (user.getUuid() == userdb.getUuid() ) {
            result = "true";
            String database = userdb.getDatabase();
            db = database;
        }
        else
            db = "false";

        //String node = service.getLoadBalance().roundRobin(db);
        //service.getLoadBalance().setUserToNode(String.valueOf(uuid),node);
        String node  = loadBalance.roundRobin(db);
        loadBalance.setUserToNode(String.valueOf(uuid),node);

        if (node == null)
            return db;

        result = node;
        return result;

    }

    public String register(UsersDB userdb) {

        Gson json = new Gson();
        int objNum = SharedClass.checkForDocuments(userDatabase.getUniqueIndex());
        String filename = String.valueOf(objNum);
        //    synchronized (this) {
        logger.info(userdb.getUuid()+" "+userdb.getUsername()+" "+userdb.getPassword());
        logger.info(userDatabase.getDbName());
        logger.info(userDatabase.getDirectoryDB().getDATABASE_DIR());
        logger.info(userDatabase.getDirectoryDB().getCOLLECTION_DIR());
        String dir = userDatabase.getDirectoryDB().getCOLLECTION_DIR();
        userdb.setUuid(objNum);
        logger.info(userdb.getUuid()+ " "+userdb.getUsername()+" "+userdb.getPassword()+" "+userdb.getDatabase());
        try (Writer writer = new FileWriter(dir + /*filename +*/ userdb.getUsername() +".json")) {
            json.toJson(userdb, writer);
            userDatabase.addUniqueIndex(userdb.getUuid());
            userDatabase.addPropertyIndex(userdb.getUsername(),String.valueOf(userdb.getUuid()));
        }catch (IOException e) {
            e.printStackTrace();
        }
        return "OK! User created";
    }


    /*public UsersDBToken auth(UsersDB userdb) {

        UsersDBToken userdbToken = new UsersDBToken(userdb.getUsername(),userdb.getPassword());
        CreateJWT createUserdbToken = new CreateJWT(userdbToken);
        String dir = userDatabase.getDirectoryDB().getCOLLECTION_DIR();
        userdbToken.setToken(createUserdbToken.getJWTToken());
        Gson json = new Gson();
        try (Writer writer = new FileWriter(dir + userdb.getUsername() + "Key.json")) {
            json.toJson(userdbToken, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return userdbToken;

    }*/
}
