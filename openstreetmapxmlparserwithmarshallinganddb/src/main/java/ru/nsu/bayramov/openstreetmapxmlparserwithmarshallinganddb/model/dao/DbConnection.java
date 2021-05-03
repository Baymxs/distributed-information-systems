package ru.nsu.bayramov.openstreetmapxmlparserwithmarshallinganddb.model.dao;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Bayramov Nizhad
 */
public abstract class DbConnection {
    public static final int BATCH_SIZE = 1000;
    private static final Logger logger = LoggerFactory.getLogger(DaoProvider.class);
    private static final String DB_STRUCTURE_PATH = "db.sql";
    private static final String DB_URL = "jdbc:postgresql://localhost:5439/postgres";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "postgres";
    private static final int BUF_SZ = 2048;
    private static Connection connection;

    public static void connect() throws IOException, SQLException {
        logger.info("Connect to database: started");
        InputStream dbStructureStream = ClassLoader.getSystemClassLoader().getResourceAsStream(DB_STRUCTURE_PATH);
        assert dbStructureStream != null;

        connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        connection.setAutoCommit(false);
        logger.info("Connect to database: complete");

        logger.info("Init database structure: started");
        String dbStructureSql = IOUtils.toString(dbStructureStream, StandardCharsets.UTF_8);
        connection.createStatement().execute(dbStructureSql);
        logger.info("Init database structure: complete");
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void close() throws SQLException {
        if (connection != null) {
            logger.info("Disconnect from database: started");
            connection.close();
            logger.info("Disconnect from database: complete");
        }
    }
}
