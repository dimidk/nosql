package com.example.nosql;

import com.example.nosql.schema.UsersDB;
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
    @Autowired
    public AdminManager(){}



    public String connect(UsersDB userdb) {

        String result = null;
        String db = null;
        int uuid = userdb.getUuid();
        UsersDB user = SharedClass.fromJsonUser(uuid);
        if (user.getUuid() == userdb.getUuid()) {
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

        result = node;
        return result;

    }

    public String createUser(UsersDB userdb) {

        String result = null;
        int uuid = userdb.getUuid();
        //int objNum = SharedClass.checkForDocuments(service.getUserDatabase().getUniqueIndex());
        int objNum = SharedClass.checkForDocuments(userDatabase.getUniqueIndex());
        String filename = String.valueOf(objNum);
        userdb.setUuid(objNum);
   /*     Gson json = new Gson();
        String dir = service.getUserDatabase().getDirectoryDB().getCOLLECTION_DIR();
        try (Writer writer = new FileWriter(dir + filename + ".json")) {
            json.toJson(userdb, writer);
            service.getUserDatabase().addUniqueIndex(userdb);
            service.getUserDatabase().addPropertyIndex(userdb);
        }catch (IOException e) {
            e.printStackTrace();
        }*/


        return result;

    }
}
