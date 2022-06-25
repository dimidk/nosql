package com.example.nosql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@Component
public class InitialService {

    private static Logger logger = LogManager.getLogger(InitialService.class);

    /*
    make static fields enumeration
     */
    private static InitialService initialThread;
    @Autowired
    private MasterDB masterDB;
    @Autowired
    private MasterDB userDatabase;
    //private PrimitiveDatabase database;
    @Autowired
    private AdminManager adminManager = new AdminManager();
    @Autowired
    private LoadBalance loadBalance = new LoadBalance();


    private InitialService() {
    }

    public static InitialService getInitialService() {

        if (initialThread == null) {
            initialThread = new InitialService();

        }
        else {
            logger.info("There is already an instance");
            throw new IllegalArgumentException();
        }

        return initialThread;
    }

    public AdminManager getAdminManager() {
        return adminManager;
    }

    public void setAdminManager(AdminManager adminManager) {
        this.adminManager = adminManager;
    }

    public  LoadBalance getLoadBalance() {
        return loadBalance;
    }

    public  void setLoadBalance(LoadBalance loadBalance) {
        this.loadBalance = loadBalance;
    }

    public MasterDB getMasterDB() {
        return masterDB;
    }

    public void setMasterDB(MasterDB masterDB) {
        this.masterDB = masterDB;
    }

    public MasterDB getUserDatabase() {
        return userDatabase;
    }

    public void setUserDatabase(MasterDB userDatabase) {
        this.userDatabase = userDatabase;
    }

    public List<Path> listFiles(Path path) throws IOException {

        List<Path> result;
        try (Stream<Path> walk = Files.walk(path)) {
            result = walk.filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        }
        return result;
    }

    public boolean dbDirExists() {

        if (!Files.exists(Path.of(masterDB.getDirectoryDB().getDATABASE_DIR()))) {

        //if (!Files.exists(Path.of(InitialService.DATABASE_DIR))) {
            return false;
        }

        return true;
    }

    public void createDbDir() {

        if (!dbDirExists()) {
            Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxr-xr-x");
            FileAttribute<Set<PosixFilePermission>> fileAttributes = PosixFilePermissions.asFileAttribute(permissions);
            try {
                Files.createDirectories(Path.of(masterDB.getDirectoryDB().getCOLLECTION_DIR()), fileAttributes);
                logger.info("create main database directory");

            }catch (IOException e) {
                e.printStackTrace();
            }
        }

        /*Thread mainNode = new Thread(new PrimitiveDatabase(initialThread));
        mainNode.start();
        logger.info(mainNode.getName());*/
    }
}
