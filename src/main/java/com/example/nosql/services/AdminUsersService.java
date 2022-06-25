package com.example.nosql.services;

import com.example.nosql.schema.Student;
import com.example.nosql.schema.UsersDB;
import org.springframework.http.HttpStatus;

public class AdminUsersService implements AdminInterface {
    @Override
    public HttpStatus write(Student student) {
        return null;
    }

    @Override
    public void write(UsersDB userdb) {

    }

    @Override
    public void update(String uuid) {

    }

    @Override
    public void delete(String uuid) {

    }

    @Override
    public void export_db(String dbName) {

    }

    @Override
    public void import_db(String dbName) {

    }
}
