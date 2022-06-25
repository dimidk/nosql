package com.example.nosql.schema;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Student implements Comparable{

    private static Integer studObj = 0;
    @JsonProperty("uuid")
    private int uuid;
    @JsonProperty("surname")
    private String surname;
    @JsonProperty("grade")
    private String grade;

    public Student() {}

    public Student(String surname, String grade) {

        this.uuid = studObj++;
        this.surname = surname;
        this.grade = grade;
    }

    public static Integer getStudObj() {
        return studObj;
    }

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
