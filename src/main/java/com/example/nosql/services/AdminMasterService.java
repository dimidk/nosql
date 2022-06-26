package com.example.nosql.services;

import com.example.nosql.InitialService;
import com.example.nosql.schema.Student;
import com.example.nosql.schema.UsersDB;
import com.example.nosql.shared.SharedClass;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
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
import java.util.*;

@Component
public class AdminMasterService implements AdminInterface {

    @Autowired
    private InitialService service ;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private SharedClass sharedClass;

    private Logger logger = LogManager.getLogger(AdminMasterService.class);
    @Autowired
    public AdminMasterService(InitialService service) {

        this.service = service;
        //super(service);
        //    SharedClass sharedClass = new SharedClass(restTemplate);
    }

    public HttpStatus write(Student student) {

        if (service == null)
            System.exit(-1);

        logger.info("get a student object");
        logger.info(service.getMasterDB().getDbName());
        logger.info(service.getUserDatabase().getDbName());
        logger.info(service.getMasterDB().getDirectoryDB().getCOLLECTION_DIR());
        if (!service.dbDirExists())
            service.createDbDir();
        Gson json = new Gson();
        int objNum = SharedClass.checkForDocuments(service.getMasterDB().getUniqueIndex());
        String filename = String.valueOf(objNum);
        //    synchronized (this) {
        String dir = service.getMasterDB().getDirectoryDB().getCOLLECTION_DIR();
        try (Writer writer = new FileWriter(dir + filename + ".json")) {
            student.setUuid(objNum);
            json.toJson(student, writer);
            logger.info("write new student to db");
            logger.info("add new student to indexes:"+student.getUuid() +student.getSurname()+student.getGrade());
            logger.info("indexes size unique,property:"+service.getMasterDB().getUniqueIndex().size()+ service.getMasterDB().getPropertyIndex().size());
            service.getMasterDB().addUniqueIndex(student.getUuid());
            logger.info("unique index:"+service.getMasterDB().getUniqueIndex().size());
            service.getMasterDB().addPropertyIndex(student.getSurname(),String.valueOf(student.getUuid()));
            logger.info("unique index:"+service.getMasterDB().getPropertyIndex().size());
        } catch (IOException e) {
            e.printStackTrace();

        }
        //SharedClass sharedClass = new SharedClass();
        List<Student> students = sharedClass.makeRestTemplateRequest("update-db");
        HttpStatus status = SharedClass.returnStatus(students);
        //        this.notifyAll();
        //    }

        return status;

    }

    @Override
    public void write(UsersDB userdb) {

    }


    public void update(String field) {

        //update wants two parameters one uuid and one the update field

        Student student = null;
        student = sharedClass.fromJson(Integer.valueOf(field),service.getMasterDB());
        student.setGrade(field);

    }


    public  void delete(String uuid)  {

        Student student = null;
        student = sharedClass.fromJson(Integer.valueOf(uuid),service.getMasterDB());


        service.getMasterDB().deletePropertyIndex(student.getSurname(),String.valueOf(student.getUuid()));
        service.getMasterDB().deleteUniqueIndex(student.getUuid());
        try {
            Files.delete(Path.of(service.getMasterDB().getDirectoryDB().getCOLLECTION_DIR() + uuid + ".json"));
        }catch (IOException e) {
            e.printStackTrace();
        }

        List<Student> students = sharedClass.makeRestTemplateRequest("update-db");
        HttpStatus status = SharedClass.returnStatus(students);

    }

    public  void export_db(String dbName) {

        logger.info("export database");

        Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxr-xr-x");
        FileAttribute<Set<PosixFilePermission>> fileAttributes = PosixFilePermissions.asFileAttribute(permissions);
        try {
            String filename = service.getMasterDB().getDirectoryDB().getDATABASE_DIR() + "datafile.txt";
            if (!Files.exists(Path.of(filename)))
                Files.createFile(Path.of(filename),fileAttributes);

            TreeSet<Integer> uniqIndex = service.getMasterDB().getUniqueIndex();
            TreeMap<String,List<String>> propIndex = service.getMasterDB().getPropertyIndex();

            logger.info("write each record to datafile");
            Files.write(Paths.get(filename), "create database file\n".getBytes(), StandardOpenOption.APPEND);
            Files.write(Paths.get(filename), "Documents\n".getBytes(), StandardOpenOption.APPEND);
            Files.write(Paths.get(filename), service.getMasterDB().getDirectoryDB().getCOLLECTION_DIR().getBytes(), StandardOpenOption.APPEND);
            uniqIndex.stream().sorted().forEach(s -> {

                Student student = sharedClass.fromJson(s,service.getMasterDB());
                Gson json = new Gson();
                String jsonString = json.toJson(student);
                try {
                    Files.write(Paths.get(filename), jsonString.getBytes(), StandardOpenOption.APPEND);
                    Files.write(Paths.get(filename), "\n".getBytes(), StandardOpenOption.APPEND);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
            logger.info("write unique index in datafile");
            Files.write(Paths.get(filename), "Unique Index of Documents\n".getBytes(), StandardOpenOption.APPEND);
            uniqIndex.stream().sorted().forEach(s -> {

                try {
                    Files.write(Paths.get(filename), String.valueOf(s).getBytes(), StandardOpenOption.APPEND);
                    Files.write(Paths.get(filename), "\n".getBytes(), StandardOpenOption.APPEND);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
            logger.info("write property index in datafile");
            Files.write(Paths.get(filename), "Property Index of Documents\n".getBytes(), StandardOpenOption.APPEND);
            for (Map.Entry s:propIndex.entrySet()) {
                String name = (String) s.getKey();
                List<String> uuids = (List<String>) s.getValue();

                for (String uuid: uuids) {
                    try {
                        Files.write(Paths.get(filename), name.getBytes(), StandardOpenOption.APPEND);
                        Files.write(Paths.get(filename), "\t".getBytes(), StandardOpenOption.APPEND);
                        Files.write(Paths.get(filename), uuid.getBytes(), StandardOpenOption.APPEND);
                        Files.write(Paths.get(filename), "\n".getBytes(), StandardOpenOption.APPEND);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void import_db(String dbName){

        logger.info("try to load/import database");

        try {
            service.getMasterDB().createDbDir();
            String dir = service.getMasterDB().getDirectoryDB().getCOLLECTION_DIR();
            service.getMasterDB().loadDatabase(dir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
