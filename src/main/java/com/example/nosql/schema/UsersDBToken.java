package com.example.nosql.schema;

public class UsersDBToken {

    private String username;
    private String token;
    private String database;

    public UsersDBToken() {}

    public UsersDBToken(String username, String token,String database) {
        this.username = username;
        this.token = token;
        this.database = database;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDatabase() { return database;}

    public void setDatabase(String database) { this.database = database;}
}
