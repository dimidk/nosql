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

        MasterDB masterDB = new MasterDB(PrimitiveDatabase.server);
        //masterDB.setDirectoryDB(directoryBean());
        //masterDB.setDirectoryDB(directoryClass);
        return masterDB;
    };

    @Bean
    public MasterDB userDatabase() {

        MasterDB userDatabase = new MasterDB(PrimitiveDatabase.server);
        return userDatabase;
    };
}
