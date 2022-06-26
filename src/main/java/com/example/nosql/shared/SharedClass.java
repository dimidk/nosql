package com.example.nosql.shared;

import com.example.nosql.InitialService;
import com.example.nosql.MasterDB;
import com.example.nosql.schema.Student;
import com.example.nosql.schema.UsersDB;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//@Component
public class SharedClass {

    private static Logger logger = LogManager.getLogger(SharedClass.class);
    @Autowired
    private  RestTemplate restTemplate;
   // @Autowired
   // private MasterDB masterDB;
   // @Autowired
   // private MasterDB userDatabase;

    @Autowired
    public SharedClass(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    };

    public static int checkForDocuments(TreeSet<Integer> index) {

        int numOfDocuments = index.size();
        return numOfDocuments++;
    }

    /*public String getDirUsers() {

        return userDatabase.getDirectoryDB().getCOLLECTION_DIR();
    }

    public String getDirMaster() {

        return masterDB.getDirectoryDB().getCOLLECTION_DIR();
    }*/

    public  Student fromJson(int field,MasterDB db) {

        Student student = null;
        Gson json = new Gson();

        String dir = null;
        String filename = null;
        dir = db.getDirectoryDB().getCOLLECTION_DIR();
        //String dir = this.masterDB.getDirectoryDB().getCOLLECTION_DIR();
        /*if (db.getDbName().equals("db"))
            filename = String.valueOf(field);
        else
            filename =*/

        try (Reader reader = new FileReader(dir+String.valueOf(field)+".json")) {
            student = json.fromJson(reader,Student.class);
            logger.info(student.getUuid());

        }catch (IOException e ){
            e.printStackTrace();
        }
        return student;
    }

//    public  UsersDB fromJsonUser(int field) {
    public  UsersDB fromJsonUser(String field,MasterDB userDatabase) {

        UsersDB userdb = null;
        Gson json = new Gson();
        //String dirUsers = getDirUsers();
        logger.info("testing for getting a message from UsersDB");
        logger.info(userDatabase.getDbName()+" with dir:"+userDatabase.getDirectoryDB().getDATABASE_DIR());
        String dir = userDatabase.getDirectoryDB().getCOLLECTION_DIR();
        logger.info("shared class retrieving for usersdb dir:"+dir);
        logger.info("try to see if user:"+field +" exists");
        try (Reader reader = new FileReader(dir+(field)+".json")) {
            userdb = json.fromJson(reader,UsersDB.class);
            logger.info(userdb.getUuid());

        }catch (IOException e ){
            e.printStackTrace();
        }
        return userdb;
    }

    public  List<Student> makeRestTemplateRequest(String restUrl) {

        logger.info("make Rest Template Request");
        ResponseEntity<List<Student>> responseEntity = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        responseEntity = restTemplate.exchange("http://localhost:8040/"+restUrl,
                //    responseEntity = restTemplate.exchange("http://slavedb:9080/read",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {}
        );
        List<Student> students = responseEntity.getBody();
        //     List<Student> students = (List<Student>) restTemplate.getForObject("http://localhost:9080/read",Student.class);
        //    responseEntity = restTemplate.getForEntity("http://dbsrv-app:8060/course/"+courseName,StudGrades[].class);

        /*HttpStatus status = null;
        if ( students != null) {
            status = HttpStatus.OK;
        }
        else
            status = HttpStatus.BAD_REQUEST;*/

        return students;
    }

    public List<Path> listFiles(Path path) throws IOException {

        List<Path> result;
        try (Stream<Path> walk = Files.walk(path)) {
            result = walk.filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        }
        return result;
    }


    public static HttpStatus returnStatus(List<Student> students) {

        logger.info("return status request");
        if (students != null)
            return HttpStatus.OK;
        else
            return HttpStatus.BAD_REQUEST;

    }


}
