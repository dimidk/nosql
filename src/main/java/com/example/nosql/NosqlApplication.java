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
        MasterDB masterDB = context.getBean("masterDB",MasterDB.class);
        MasterDB userDatabase = context.getBean("userDatabase",MasterDB.class);

        System.out.println(masterDB.getDbName());
       /* @Bean
        MasterDB masterDB(){
            return context.getBean("masterDB",MasterDB.class);
        } ;
        @Bean
        MasterDB userDatabase() {

            return context.getBean("userDatabase",MasterDB.class);

        } ;*/


        server.initializeDatabases();



    }

}
