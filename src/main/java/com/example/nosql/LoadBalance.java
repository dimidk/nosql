package com.example.nosql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Queue;
@Component
public class LoadBalance  {

    private static Logger logger = LogManager.getLogger(LoadBalance.class);

    private Queue<String> databaseInstance = new LinkedList<>();

    @Autowired
    public LoadBalance() {
        this.databaseInstance = new LinkedList<>();
    }



    public void roundRobin(String database) {


    }
}
