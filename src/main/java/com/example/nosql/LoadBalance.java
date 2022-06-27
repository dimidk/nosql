package com.example.nosql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
@Component
public class LoadBalance  {

    private static Logger logger = LogManager.getLogger(LoadBalance.class);

    private Queue<String> databaseInstance = new LinkedList<>();
    @Autowired
    private MasterDB userDatabase;

    private HashMap<String,String> userAndNode = new HashMap<String, String>();

    @Autowired
    public LoadBalance() {
        this.databaseInstance = new LinkedList<>();
        /*this.databaseInstance.add("http://node-1:8040/");
        this.databaseInstance.add("http://node-2:8050/");
        this.databaseInstance.add("http://node-3:8060/");*/

        this.databaseInstance.add("http://localhost:8040/");
        this.databaseInstance.add("http://localhost:8050/");
        this.databaseInstance.add("http://localhost:8060/");

    }


    public HashMap<String,String> setUserToNode(String uuid,String node) {

         userAndNode.put(uuid,node);
         return userAndNode;
    }

    public String roundRobin(String database) {

        logger.info("load balance:"+database);
        String available = databaseInstance.poll();
        logger.info("get first element:"+available);
        logger.info(databaseInstance.size());
        for (String ele:databaseInstance) {
            logger.info(ele);
        }
        logger.info("adding at the end");
        databaseInstance.add(available);
        for (String ele:databaseInstance) {
            logger.info(ele);
        }

        return available;
    }
}
