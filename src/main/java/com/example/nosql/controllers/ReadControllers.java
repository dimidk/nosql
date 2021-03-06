package com.example.nosql.controllers;

import com.example.nosql.InitialService;
import com.example.nosql.schema.Student;
import com.example.nosql.shared.SharedClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
@RestController
@Configuration
public class ReadControllers {

    @Autowired
    private RestTemplate restTemplate;
    private HashMap<String, String> userAndNode = new HashMap<>();
    @Autowired
    private InitialService service;

    /*@Autowired
    private ManageCRUDServices services;*/
    @Autowired
    private SharedClass sharedClass;

    private Logger logger = LogManager.getLogger(ReadControllers.class);

    @RequestMapping(value="/read",method= RequestMethod.GET,
            consumes = {"*/*"})
    public HttpStatus read() {

        logger.info("return all students");

        HttpStatus status = null;
        List<Student> students = sharedClass.makeRestTemplateRequest("read");
        status = SharedClass.returnStatus(students);

        return status;
    }

    @RequestMapping(value="/read/{uuid}",method= RequestMethod.GET,
            consumes = {"*/*"})
    public Student read(@PathVariable String uuid) {

        logger.info("return student with uuid:"+uuid);
        /*Student student = services.read(uuid);
        return student;*/

        HttpStatus status = null;
        List<Student> students = sharedClass.makeRestTemplateRequest("read/"+uuid);
        status = SharedClass.returnStatus(students);


        return students.get(0);
    }

    @RequestMapping(value="/read/stud-name",method= RequestMethod.GET,
            consumes = {"*/*"})
    public List<Student> read_name(){

        logger.info("read students by name");
        /*List<Student> lStud = services.displayByName();
        return lStud;*/


        HttpStatus status = null;
        List<Student> students = sharedClass.makeRestTemplateRequest("read/stud-name");
        status = SharedClass.returnStatus(students);

        return students;
    }

    @GetMapping("/read/stud-name/{surname}")
    public List<Student> read_name(@PathVariable String surname){

        logger.info("read students with name:"+surname);
        /*List<Student> students = services.findStud(surname);
        return students;*/

        HttpStatus status = null;
        List<Student> students = sharedClass.makeRestTemplateRequest("read/stud-name/"+surname);
        status = SharedClass.returnStatus(students);

        return students;
    }

}
