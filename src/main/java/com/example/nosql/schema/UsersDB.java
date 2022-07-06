package com.example.nosql.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UsersDB implements UserDetails {


    private static Integer userObj=0;

    @JsonProperty("uuid")
    int uuid;
    @JsonProperty("username")
    String username;
    @JsonProperty("password")
    String password;

    @JsonProperty
    String role;
    @JsonProperty("database")
    String database;

    public UsersDB(){}

    public UsersDB(String username, String password, String role,String database) {
        this.uuid = userObj++;
        this.username = username;
        this.password = password;
        this.role = role;
        this.database = database;
    }

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {return role;}

    public void setRole(String role) {this.role = role;}

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
