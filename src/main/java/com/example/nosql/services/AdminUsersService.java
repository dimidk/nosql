package com.example.nosql.services;

import com.example.nosql.schema.Student;
import com.example.nosql.schema.UsersDB;
import org.springframework.http.HttpStatus;

public class AdminUsersService implements AdminInterface,AdminUsersInterface {

    @Override
    public HttpStatus delete(String uuid) {
        return null;

    }

    @Override
    public HttpStatus export_db(String dbName) {
        return null;
    }

    @Override
    public HttpStatus import_db(String dbName) {
        return null;
    }

    @Override
    public void write(UsersDB userdb) {

    }
}
