package com.example.nosql.services;

import com.example.nosql.schema.Student;
import com.example.nosql.schema.UsersDB;
import org.springframework.http.HttpStatus;

public interface AdminMasterInterface extends AdminInterface {

    public HttpStatus write(Student student);
    public Student update_grade(String uuid,String field);
    public Student update_surname(String uuid,String field);
}
