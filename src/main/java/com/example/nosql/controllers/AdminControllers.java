package com.example.nosql.controllers;

import com.example.nosql.schema.Student;
import com.example.nosql.services.AdminMasterService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.io.IOException;
@RestController
@Configuration
public class AdminControllers {

    @Autowired
    // private ManagerInterface adminServices;
    private AdminMasterService adminServices;

    private Logger logger = LogManager.getLogger(AdminControllers.class);

    @GetMapping("/export/{dbName}")
    public void export(@PathVariable String dbName) {

        logger.info("export database");
        adminServices.export_db(dbName);

    }

    @GetMapping("/import/{dbName}")
    public void import_db(@PathVariable String dbName) {

        logger.info("import database");
        adminServices.import_db(dbName);

    }

    @PostMapping("/write")
    @RolesAllowed("ROLE_ADMIN")
    public void write(@RequestBody Student student) {

        logger.info("write for student:"+student);
        logger.info(student.getUuid()+" "+student.getSurname());
        if (adminServices == null) {
            logger.info("no normal exit");
            System.exit(-1);
        }
        adminServices.write(student);

    }


    @PutMapping("/update/{uuid}/grade/{field}")
    public Student update_grade(@PathVariable String uuid,@PathVariable String field) {

        logger.info("update certain student");
        Student student = adminServices.update_grade(uuid,field);

        return student;

    }

    @PutMapping("/update/{uuid}/surname/{field}")
    public Student update_surname(@PathVariable String uuid,@PathVariable String field) {

        logger.info("update certain student");
        Student student = adminServices.update_surname(uuid,field);

        return student;

    }


    @DeleteMapping("/delete/{uuid}")
    public void delete(@PathVariable String uuid) throws IOException {

        logger.info("delete certain student");
        adminServices.delete(uuid);

    }

}
