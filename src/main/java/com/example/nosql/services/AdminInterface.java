package com.example.nosql.services;

import com.example.nosql.schema.Student;
import com.example.nosql.schema.UsersDB;
import org.springframework.http.HttpStatus;

public interface AdminInterface {


    public HttpStatus write(Student student);
    public void write(UsersDB userdb);
    public void update(String uuid);
    public void delete(String uuid);
    public void export_db(String dbName);
    public void import_db(String dbName);

}
