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
        dirMaster.setDATABASE_DIR("db/");
        dirMaster.setCOLLECTION_DIR("db/student/");
        logger.info(dirMaster.getCOLLECTION_DIR()+" "+dirMaster.getDATABASE_DIR());
        MasterDB masterDB = new MasterDB(/*PrimitiveDatabase.server,*/dirMaster,"db");
        logger.info("create new bean master db");
        //masterDB.setDirectoryDB(dirMaster);
        //masterDB.setDbName("db");
        /*if (masterDB.dbDirExists()) {
            try {
                masterDB.loadDatabase(masterDB.getDirectoryDB().getCOLLECTION_DIR());
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        else*/
        //    masterDB.createDbDir();
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
        dirUser.setDATABASE_DIR("usersDB");
        dirUser.setCOLLECTION_DIR("usersDB/");
        MasterDB userDatabase = new MasterDB(/*PrimitiveDatabase.server,*/dirUser,"usersDB");
        //userDatabase.setDirectoryDB(dirUser);
        //userDatabase.setDbName("usersDB");
        /*if (userDatabase.dbDirExists()) {
            try {
                userDatabase.loadDatabase(userDatabase.getDirectoryDB().getCOLLECTION_DIR());
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        else*/
        //    userDatabase.createDbDir();
        try {
            userDatabase.loadDatabase(dirUser.getCOLLECTION_DIR());
        }catch (IOException e) {
            e.printStackTrace();
        }

        return userDatabase;
    };
}
