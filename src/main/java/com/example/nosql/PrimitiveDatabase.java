package com.example.nosql;

import com.example.nosql.schema.Student;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

public abstract class PrimitiveDatabase {

    private DirectoryClass directoryDB;
    protected static InitialService server;

    private TreeSet<Integer> uniqueIndex = new TreeSet<>();
    private TreeMap<String, List<String>> propertyIndex = new TreeMap<>();

    public PrimitiveDatabase(InitialService server) {

        this.server = server;
    }

    public DirectoryClass getDirectoryDB() {
        return directoryDB;
    }

    public void setDirectoryDB(DirectoryClass directoryDB) {
        this.directoryDB = directoryDB;
    }

    public TreeSet<Integer> getUniqueIndex() {
        return uniqueIndex;
    }

    public void setUniqueIndex(TreeSet<Integer> uniqueIndex) {
        this.uniqueIndex = uniqueIndex;
    }

    public TreeMap<String, List<String>> getPropertyIndex() {
        return propertyIndex;
    }

    public void setPropertyIndex(TreeMap<String, List<String>> propertyIndex) {
        this.propertyIndex = propertyIndex;
    }

//   public void addPropertyIndex(Student stud) {
    public void addPropertyIndex(String name, String uuid) {

        if (name == null) {
            //    logger.info("ERROR");
            throw new IllegalArgumentException();
        }
        //    if (this.getPropertyIndex().containsKey(stud.getSurname()))
        if (propertyIndex.containsKey(name))
            propertyIndex.get(name).add(String.valueOf(uuid));
        else {
            propertyIndex.put(name,new ArrayList<>());
            propertyIndex.get(name).add(String.valueOf(uuid));
        }
    }



//    public void deletePropertyIndex(Student stud) {
    public void deletePropertyIndex(String name,String uuid) {

        if (name == null)
            throw new IllegalArgumentException();

        List<String> temp = this.getPropertyIndex().get(name);
        propertyIndex.remove(name,temp);

        temp.remove(String.valueOf(uuid));
        propertyIndex.put(name,temp);
    }

    public void addUniqueIndex(int uuid) {

        if (uuid == -1) {
            throw new IllegalArgumentException();
        }
        uniqueIndex.add(uuid);
    }

    public void deleteUniqueIndex(int uuid) {


        if (uuid == -1) {
            throw new IllegalArgumentException();
        }
        uniqueIndex.remove(uuid);

    }

    public abstract void createDbDir();

    public abstract void loadDatabase(String dir) throws IOException;

    public abstract void createSlaveDB(String slaveDir);

}
