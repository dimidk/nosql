package com.example.nosql.services;

import com.example.nosql.InitialService;
import com.example.nosql.MasterDB;
import com.example.nosql.schema.Student;
import com.example.nosql.schema.UsersDB;
import com.example.nosql.shared.SharedClass;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.*;

@Component
public class AdminMasterService implements AdminInterface,AdminMasterInterface {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private MasterDB masterDB;
    private SharedClass sharedClass = new SharedClass(restTemplate);

    private Logger logger = LogManager.getLogger(AdminMasterService.class);
    @Autowired
    public AdminMasterService(InitialService service) {}


    protected  List<Student> makeRestTemplateRequest(String restUrl) {

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

    protected  Student makeSingleRestTemplateRequest(String restUrl) {

        ResponseEntity<Student> responseEntity ;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = "http://localhost:8060/"+restUrl;
        logger.info("url request:"+url);
        responseEntity = restTemplate.exchange(url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Student>() {}
        );
        Student student = responseEntity.getBody();
        return student;
    }



    public HttpStatus write(Student student) {

        logger.info("write a student object");

        Gson json = new Gson();
        int objNum = SharedClass.checkForDocuments(masterDB.getUniqueIndex());
        String filename = String.valueOf(objNum);
        synchronized (this) {
            String dir = masterDB.getDirectoryDB().getCOLLECTION_DIR();
            try (Writer writer = new FileWriter(dir + filename + ".json")) {
                student.setUuid(objNum);
                json.toJson(student, writer);

                logger.info("add new student to indexes");
                masterDB.addUniqueIndex(student.getUuid());
                masterDB.addPropertyIndex(student.getSurname(), String.valueOf(student.getUuid()));
            } catch (IOException e) {
                e.printStackTrace();

            }
            this.notifyAll();
        }
        List<Student> students = makeRestTemplateRequest("update-db");
        HttpStatus status = SharedClass.returnStatus(students);

        return status;

    }

    public Student update_grade(String uuid,String field) {



        Student student = null;
        student = sharedClass.fromJson(Integer.valueOf(uuid),masterDB);

        String dir = masterDB.getDirectoryDB().getCOLLECTION_DIR();
        Gson json = new Gson();
        try (Writer writer = new FileWriter(dir + String.valueOf(uuid) + ".json")) {
            student.setGrade(field);
            json.toJson(student, writer);

        }catch (IOException e ){
            e.printStackTrace();
        }

        /*List<Student> students = makeRestTemplateRequest("update-db");
        HttpStatus status = SharedClass.returnStatus(students);*/

        return student;

    }

    /*protected String getKeyByValue(TreeMap<String,List<String>> tree, String value) {

        String findKey = null;


        for (Map.Entry<String, List<String>> e: tree.entrySet()) {
            String key = (String) e.getKey();
            logger.info(key);
            for (String s :e.getValue()) {
                if (s.equals(value)) {
                    findKey = key;
                    break;
                }
            }
        }

        return findKey;
    }*/

//    protected void updatePropertyIndex(String uuid,String field) {
    protected void updatePropertyIndex(String uuid,String oldSurname,String newSurname) {

    //    String dir = masterDB.getDirectoryDB().getCOLLECTION_DIR();

        TreeMap<String, List<String>> temp = masterDB.getPropertyIndex();

        if (temp.get(oldSurname).size() == 1) {
            List<String> uuidList = masterDB.getPropertyIndex().get(oldSurname);
            masterDB.getPropertyIndex().remove(oldSurname,uuidList);
        }
        else
            masterDB.getPropertyIndex().get(oldSurname).remove(uuid);

        if (!temp.containsKey(newSurname)) {
            masterDB.getPropertyIndex().put(newSurname,new ArrayList<>());
            masterDB.getPropertyIndex().get(newSurname).add(uuid);
        }
        else {
            masterDB.getPropertyIndex().get(newSurname).add(uuid);
        }




    }

    public Student update_surname(String uuid,String field) {

        //update wants two parameters one uuid and one the update field

        Student student = null;
        String oldSurname = null;
        student = sharedClass.fromJson(Integer.valueOf(uuid),masterDB);

        String dir = masterDB.getDirectoryDB().getCOLLECTION_DIR();
        Gson json = new Gson();
        try (Writer writer = new FileWriter(dir + String.valueOf(uuid) + ".json")) {

            oldSurname = student.getSurname();
            student.setSurname(field);
            json.toJson(student, writer);

        }catch (IOException e ){
            e.printStackTrace();
        }

        updatePropertyIndex(uuid,oldSurname,field);

        logger.info("update to property index surname");
        student = makeSingleRestTemplateRequest("update-json/"+uuid);
        //HttpStatus status = SharedClass.returnStatus(student);

        return student;

    }


    public  HttpStatus delete(String uuid)  {

        logger.info("delete student");

        Student student = null;
        student = sharedClass.fromJson(Integer.valueOf(uuid),masterDB);

        masterDB.deletePropertyIndex(student.getSurname(),String.valueOf(student.getUuid()));
        masterDB.deleteUniqueIndex(student.getUuid());
        try {
            Files.delete(Path.of(masterDB.getDirectoryDB().getCOLLECTION_DIR() + uuid + ".json"));
        }catch (IOException e) {
            e.printStackTrace();
        }

        List<Student> students = makeRestTemplateRequest("update-db");
        HttpStatus status = SharedClass.returnStatus(students);

        return status;
    }

    public  HttpStatus export_db(String dbName) {

        logger.info("export database");

        Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxr-xr-x");
        FileAttribute<Set<PosixFilePermission>> fileAttributes = PosixFilePermissions.asFileAttribute(permissions);
        try {
            String filename = masterDB.getDirectoryDB().getDATABASE_DIR() + "databaseDB.json";
            if (!Files.exists(Path.of(filename)))
                Files.createFile(Path.of(filename),fileAttributes);
            else {
                Files.deleteIfExists(Path.of(filename));
                Files.createFile(Path.of(filename),fileAttributes);
            }
            TreeSet<Integer> uniqIndex = masterDB.getUniqueIndex();
            TreeMap<String,List<String>> propIndex = masterDB.getPropertyIndex();
            String dir = masterDB.getDirectoryDB().getCOLLECTION_DIR();

            List<Student> students = sharedClass.getAllStudents(masterDB);
            Gson json = new Gson();
            String studentsString = json.toJson(students);

            String uniqueIndexString = json.toJson(uniqIndex);
            String propIndexString = json.toJson(propIndex);

            Files.write(Paths.get(filename), studentsString.getBytes()
                    ,StandardOpenOption.APPEND);
            Files.write(Paths.get(filename), uniqueIndexString.getBytes()
                    ,StandardOpenOption.APPEND);
            Files.write(Paths.get(filename), propIndexString.getBytes()
                    ,StandardOpenOption.APPEND);

            return HttpStatus.OK;

        }catch (IOException e) {
            e.printStackTrace();
        }

        return HttpStatus.BAD_REQUEST;

    }

    public HttpStatus import_db(String dbName){

        //wrong. Δεν κάνω load από μια βάση που δεν υπάρχει. Πρέπει να δημιουργηθούν οι
        //κατάλογοι και μετά να κάνω import από το αρχείο.

        logger.info("import database");

        try {
            masterDB.createDbDir();
            String dir = masterDB.getDirectoryDB().getDATABASE_DIR();
            String lines = String.valueOf(Files.readAllLines(Path.of(dir+"databaseDB.json")));
            logger.info(lines);
            Gson json = new Gson();
            /*Type setType = new TypeToken<List<Student>>(){}.getType();
            List<Student> students = json.fromJson(lines,setType);
            students.forEach(s -> logger.info(s));*/

            return HttpStatus.OK;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
