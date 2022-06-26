package com.example.nosql;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfig  {

   /* @Bean
    public DirectoryClass directoryBean() {
        DirectoryClass directoryClass = new DirectoryClass();
        return directoryClass;
    }*/

    DirectoryClass directoryClass = new DirectoryClass();
    @Bean
    public MasterDB masterDB() {

        DirectoryClass dirMaster = new DirectoryClass();
        dirMaster.setDATABASE_DIR("db");
        dirMaster.setCOLLECTION_DIR("db/student/");
        MasterDB masterDB = new MasterDB(PrimitiveDatabase.server);
        masterDB.setDirectoryDB(dirMaster);
        masterDB.setDbName("db");
        masterDB.createDbDir();

        return masterDB;
    };

    @Bean
    public MasterDB userDatabase() {

        DirectoryClass dirUser = new DirectoryClass();
        dirUser.setDATABASE_DIR("usersDB");
        dirUser.setCOLLECTION_DIR("usersDB/");
        MasterDB userDatabase = new MasterDB(PrimitiveDatabase.server);
        userDatabase.setDirectoryDB(dirUser);
        userDatabase.setDbName("usersDB");
        userDatabase.createDbDir();

        return userDatabase;
    };
}
