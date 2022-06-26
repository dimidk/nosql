package com.example.nosql;

import com.example.nosql.schema.Student;
import com.example.nosql.schema.UsersDB;
import com.example.nosql.shared.SharedClass;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MasterDB extends PrimitiveDatabase {

    private static Logger logger = LogManager.getLogger(MasterDB.class);

    public MasterDB(/*InitialService server,*/DirectoryClass directoryClass,String dbName) {
       // super(server,directoryClass);
        super(directoryClass,dbName);
    }

    public boolean dbDirExists() {

        if (!Files.exists(Path.of(this.getDirectoryDB().getDATABASE_DIR()))) {
            return false;
        }
        return true;
    }

    @Override
    public void createDbDir() {

        if (!dbDirExists()) {
            Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxr-xr-x");
            FileAttribute<Set<PosixFilePermission>> fileAttributes = PosixFilePermissions.asFileAttribute(permissions);
            try {
                String mainDir = this.getDirectoryDB().getCOLLECTION_DIR();
                Files.createDirectories(Path.of(mainDir), fileAttributes);
                logger.info("create main database directory");

            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
            logger.info("directory exists!!");

    }

    public List<Path> listFiles(Path path) throws IOException {

        List<Path> result;
        try (Stream<Path> walk = Files.walk(path)) {
            result = walk.filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        }
        return result;
    }

    @Override
    public void loadDatabase(String dir) throws IOException {

        logger.info("directory to read from "+dir);

        TreeSet<Integer> tempTree = this.getUniqueIndex();
        TreeMap<String, List<String>> tempMap = this.getPropertyIndex();

        logger.info(tempTree.size());
        logger.info(tempMap.entrySet().size());

        if (this.getPropertyIndex().size() == 0 || this.getUniqueIndex().size() == 0) {

            if (Files.exists(Path.of(dir))) {
                logger.info("directory exists");
                //List<Path> files = PrimitiveDatabase.server.listFiles(Path.of(dir));
                //List<Path> files = server.listFiles(Path.of(dir));
                List<Path> files = listFiles(Path.of(dir));
                files.forEach(s -> {
                    Gson json = new Gson();
                    try (Reader reader = new FileReader(String.valueOf(s))) {
                        if (this.getDbName().equals("db")) {
                            Student student = json.fromJson(reader, Student.class);

                            addPropertyIndex(student.getSurname(), String.valueOf(student.getUuid()));
                            addUniqueIndex(student.getUuid());
                        }
                        else {
                            UsersDB usersDB = json.fromJson(reader,UsersDB.class);
                            addPropertyIndex(usersDB.getUsername(), String.valueOf(usersDB.getUuid()));
                            addUniqueIndex(usersDB.getUuid());
                        }
                        logger.info(getPropertyIndex().size());
                        logger.info(getUniqueIndex().size());

                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }


    }

    @Override
    public void createSlaveDB(String slaveDir) {

    }
}
