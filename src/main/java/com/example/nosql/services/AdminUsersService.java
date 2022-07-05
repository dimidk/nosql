package com.example.nosql.services;

import com.example.nosql.MasterDB;
import com.example.nosql.schema.Student;
import com.example.nosql.schema.UsersDB;
import com.example.nosql.shared.SharedClass;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class AdminUsersService implements AdminInterface,AdminUsersInterface {

    private static Logger logger = LogManager.getLogger(AdminUsersService.class);
    @Autowired
    private RestTemplate restTemplate;
    private SharedClass sharedClass = new SharedClass(restTemplate);
    @Autowired
    private MasterDB userDatabase;


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

    @Override
    public HttpStatus delete(String uuid) {

        logger.info("delete user in database");

        UsersDB usersDB = null;
        usersDB = sharedClass.fromJsonUser(uuid,userDatabase);


        userDatabase.deletePropertyIndex(usersDB.getUsername(),String.valueOf(usersDB.getUuid()));
        userDatabase.deleteUniqueIndex(usersDB.getUuid());
        try {
            Files.delete(Path.of(userDatabase.getDirectoryDB().getCOLLECTION_DIR() + uuid + ".json"));
        }catch (IOException e) {
            e.printStackTrace();
        }

        //List<Student> students = makeRestTemplateRequest("update-db");
        //HttpStatus status = SharedClass.returnStatus(students);

        //return status;

        return HttpStatus.OK;

    }

    @Override
    public HttpStatus export_db(String dbName) {

        logger.info("export users database");

        Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxr-xr-x");
        FileAttribute<Set<PosixFilePermission>> fileAttributes = PosixFilePermissions.asFileAttribute(permissions);
        try {
            String filename = userDatabase.getDirectoryDB().getDATABASE_DIR() + "databaseDB.json";
            if (!Files.exists(Path.of(filename)))
                Files.createFile(Path.of(filename),fileAttributes);
            else {
                Files.deleteIfExists(Path.of(filename));
                Files.createFile(Path.of(filename),fileAttributes);
            }
            TreeSet<Integer> uniqIndex = userDatabase.getUniqueIndex();
            TreeMap<String,List<String>> propIndex = userDatabase.getPropertyIndex();
            String dir = userDatabase.getDirectoryDB().getCOLLECTION_DIR();

            List<UsersDB> users = sharedClass.getAllUsersDB(userDatabase);
            Gson json = new Gson();
            String usersString = json.toJson(users);

            String uniqueIndexString = json.toJson(uniqIndex);
            String propIndexString = json.toJson(propIndex);

            Files.write(Paths.get(filename), usersString.getBytes()
                    , StandardOpenOption.APPEND);
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

    @Override
    public HttpStatus import_db(String dbName) {

        logger.info("import database");

        try {
            userDatabase.createDbDir();
            String dir = userDatabase.getDirectoryDB().getDATABASE_DIR();
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

    @Override
    public void write(UsersDB userdb) {

        logger.info("write a user object");

        Gson json = new Gson();
        int objNum = SharedClass.checkForDocuments(userDatabase.getUniqueIndex());
        String filename = String.valueOf(objNum);
    //    synchronized (this) {
            String dir = userDatabase.getDirectoryDB().getCOLLECTION_DIR();
            try (Writer writer = new FileWriter(dir + filename + ".json")) {
                userdb.setUuid(objNum);
                json.toJson(userdb, writer);

                logger.info("add new student to indexes");
                userDatabase.addUniqueIndex(userdb.getUuid());
                userDatabase.addPropertyIndex(userdb.getUsername(), String.valueOf(userdb.getUuid()));
            } catch (IOException e) {
                e.printStackTrace();

            }
        //    this.notifyAll();
   //     }
    //    List<Student> students = makeRestTemplateRequest("update-db");
    //    HttpStatus status = SharedClass.returnStatus(students);

       // return status;

    }
}
