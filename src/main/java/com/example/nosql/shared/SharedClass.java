package com.example.nosql.shared;

import com.example.nosql.InitialService;
import com.example.nosql.MasterDB;
import com.example.nosql.schema.Student;
import com.example.nosql.schema.UsersDB;
import com.google.gson.Gson;
import org.apache.catalina.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//@Component
public class SharedClass {

    private static Logger logger = LogManager.getLogger(SharedClass.class);
    @Autowired
    private  RestTemplate restTemplate;

    public SharedClass(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    };

    public static int checkForDocuments(TreeSet<Integer> index) {

        int numOfDocuments ;
        if (index.size() != 0)
            numOfDocuments = index.last();
        else
            numOfDocuments = 0;
        return ++numOfDocuments;
    }


    public  Student fromJson(int field,MasterDB db) {

        Student student = null;
        Gson json = new Gson();

        String dir = null;
        String filename = null;
        dir = db.getDirectoryDB().getCOLLECTION_DIR();

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
//big problem with this method. Don't work from here, only in each class
    public  List<Student> makeRestTemplateRequest(String restUrl) {

        logger.info("make Rest Template Request");
        ResponseEntity<List<Student>> responseEntity ;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //    String url = "http://localhost:8060/"+restUrl;
        String url = "http://localhost:8060/"+restUrl;
        logger.info("url request:"+url);
        responseEntity = restTemplate.exchange(url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {}
        );
        List<Student> students = responseEntity.getBody();
        return students;
    }

    public List<Student> getAllStudents(MasterDB db) {

        List<Student> students = new ArrayList<>();
        String dir = db.getDirectoryDB().getCOLLECTION_DIR();
        try {
            List<Path> files = db.listFiles(Path.of(dir));
            files.forEach(s -> {
                Gson json = new Gson();
                try (Reader reader = new FileReader(String.valueOf(s))) {
                    Student student = json.fromJson(reader, Student.class);
                    students.add(student);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }catch (IOException e) {
            e.printStackTrace();
        }
        return students;
    }

    public List<UsersDB> getAllUsersDB(MasterDB db) {

        List<UsersDB> users = new ArrayList<>();
        String dir = db.getDirectoryDB().getCOLLECTION_DIR();
        try {
            List<Path> files = db.listFiles(Path.of(dir));
            files.forEach(s -> {
                Gson json = new Gson();
                try (Reader reader = new FileReader(String.valueOf(s))) {
                    UsersDB user = json.fromJson(reader, UsersDB.class);
                    users.add(user);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static HttpStatus returnStatus(List<Student> students) {

        logger.info("return status request");
        if ((students != null) )
            return HttpStatus.OK;
        else
            return HttpStatus.BAD_REQUEST;

    }


}
