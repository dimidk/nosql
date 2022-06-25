package com.example.nosql;

import com.example.nosql.schema.Student;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

public class MasterDB extends PrimitiveDatabase {

    private static Logger logger = LogManager.getLogger(MasterDB.class);

    public MasterDB(InitialService server) {
        super(server);
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
                List<Path> files = PrimitiveDatabase.server.listFiles(Path.of(dir));
                files.forEach(s -> {
                    Gson json = new Gson();
                    try (Reader reader = new FileReader(String.valueOf(s))) {
                        Student student = json.fromJson(reader, Student.class);

                        addPropertyIndex(student);
                        addUniqueIndex(student);
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
