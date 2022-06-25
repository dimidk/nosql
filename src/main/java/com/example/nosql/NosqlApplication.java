package com.example.nosql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class NosqlApplication {

    @Autowired
    private static InitialService server = InitialService.getInitialService();

    @Bean
    RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    };

    public static void main(String[] args) {

        SpringApplication.run(NosqlApplication.class, args);

        ApplicationContext context
                = new AnnotationConfigApplicationContext(
                DatabaseConfig.class);


    //    DirectoryClass dirMaster = context.getBean("dirMaster",DirectoryClass.class);
        MasterDB masterDB = context.getBean("masterDB",MasterDB.class);
        DirectoryClass dirMaster = new DirectoryClass();
        dirMaster.setDATABASE_DIR("db");
        dirMaster.setCOLLECTION_DIR("db/student/");
        masterDB.setDirectoryDB(dirMaster);
        masterDB.createDbDir();

        DirectoryClass dirUser = new DirectoryClass();
        dirUser.setDATABASE_DIR("usersDB");
        dirUser.setCOLLECTION_DIR("usersDB/");
        MasterDB userDatabase = context.getBean("userDatabase",MasterDB.class);
        userDatabase.setDirectoryDB(dirUser);
        userDatabase.createDbDir();
    }

}
