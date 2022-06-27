package com.example.nosql.services;

import com.example.nosql.schema.UsersDB;

public interface AdminUsersInterface extends AdminInterface {

    public void write(UsersDB userdb);
}
