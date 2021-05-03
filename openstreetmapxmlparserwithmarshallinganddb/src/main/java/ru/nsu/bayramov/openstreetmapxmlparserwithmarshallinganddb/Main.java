package ru.nsu.bayramov.openstreetmapxmlparserwithmarshallinganddb;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nsu.bayramov.openstreetmapxmlparserwithmarshallinganddb.model.dao.DaoProvider;
import ru.nsu.bayramov.openstreetmapxmlparserwithmarshallinganddb.model.dao.DbConnection;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

/**
 * @author Bayramov Nizhad
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException, SQLException {
        if (args.length < 2) {
            throw new IllegalArgumentException("Usage: a.out INPUT_FILE DB_ACCESS_MODE (1 - write by prepared queries, " +
                    "2 - write by raw queries, 3 - write by batch queries)");
        }
        DbConnection.connect();
        DaoProvider.getInstance().setDbMode(Integer.parseInt(args[1]));

        logger.info("File decompression: started");
        try (InputStream inputStream = new BZip2CompressorInputStream(new FileInputStream(args[0]))) {
            logger.info("File decompression: complete");
            OsmProcessor.execute(inputStream);
        } catch (FileNotFoundException e) {
            logger.error("File not found", e);
        } catch (IOException e) {
            logger.error("IO exception", e);
        } finally {
            DaoProvider.getInstance().setDbMode(0);
            DbConnection.close();
        }
    }
}
