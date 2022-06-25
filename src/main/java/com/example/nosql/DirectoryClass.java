package com.example.nosql;

public class DirectoryClass {

    private String DATABASE_DIR;
    private String COLLECTION_DIR;

    /*public DirectoryClass(String DATABASE_DIR, String COLLECTION_DIR) {
        this.DATABASE_DIR = DATABASE_DIR;
        this.COLLECTION_DIR = COLLECTION_DIR;
    }*/
    public DirectoryClass(){}

    public String getDATABASE_DIR() {
        return DATABASE_DIR;
    }

    public void setDATABASE_DIR(String DATABASE_DIR) {
        this.DATABASE_DIR = DATABASE_DIR;
    }

    public String getCOLLECTION_DIR() {
        return COLLECTION_DIR;
    }

    public void setCOLLECTION_DIR(String COLLECTION_DIR) {
        this.COLLECTION_DIR = COLLECTION_DIR;
    }
}
