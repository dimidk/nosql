package com.example.nosql.services;

import com.example.nosql.schema.Student;
import com.example.nosql.schema.UsersDB;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.http.HttpStatus;

public interface AdminInterface {

    public HttpStatus delete(String uuid);
    public HttpStatus export_db(String dbName);
    public HttpStatus import_db(String dbName);

}
