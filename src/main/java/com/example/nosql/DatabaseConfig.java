package com.example.nosql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class DatabaseConfig  {

   /* @Bean
    public DirectoryClass directoryBean() {
        DirectoryClass directoryClass = new DirectoryClass();
        return directoryClass;
    }*/
    private static Logger logger = LogManager.getLogger(DatabaseConfig.class);

    DirectoryClass directoryClass = new DirectoryClass();
    @Bean
    public MasterDB masterDB() {

        logger.info("create bean db");
        DirectoryClass dirMaster = new DirectoryClass();
        dirMaster.setDATABASE_DIR("/home/boys/Atypon/Final/db/");
        dirMaster.setCOLLECTION_DIR("/home/boys/Atypon/Final/db/student/");
        logger.info(dirMaster.getCOLLECTION_DIR()+" "+dirMaster.getDATABASE_DIR());
        MasterDB masterDB = new MasterDB(/*PrimitiveDatabase.server,*/dirMaster,"db");
        logger.info("create new bean master db");

            masterDB.createDbDir();
        try {
            logger.info("database exists and load any");
            logger.info("from dir:"+dirMaster.getCOLLECTION_DIR());
            masterDB.loadDatabase(dirMaster.getCOLLECTION_DIR());
        }catch (IOException e) {
            e.printStackTrace();
        }
        return masterDB;
    };

    @Bean
    public MasterDB userDatabase() {

        DirectoryClass dirUser = new DirectoryClass();
        dirUser.setDATABASE_DIR("/home/boys/Atypon/Final/usersDB");
        dirUser.setCOLLECTION_DIR("/home/boys/Atypon/Final/usersDB/");
        MasterDB userDatabase = new MasterDB(/*PrimitiveDatabase.server,*/dirUser,"usersDB");
        //userDatabase.setDirectoryDB(dirUser);
        //userDatabase.setDbName("usersDB");

            userDatabase.createDbDir();
        try {
            userDatabase.loadDatabase(dirUser.getCOLLECTION_DIR());
        }catch (IOException e) {
            e.printStackTrace();
        }

        return userDatabase;
    };
}
