package ru.nsu.bayramov.openstreetmapxmlparser;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import ru.nsu.bayramov.openstreetmapxmlparser.OsmProcessor.OsmProcessingResult;

/**
 * @author Bayramov Nizhad
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Wrong number of arguments");
        }

        logger.info("File decompression: started");
        try (InputStream inputStream = new BZip2CompressorInputStream(new FileInputStream(args[0]))) {
            logger.info("File decompression: complete");
            OsmProcessingResult processingResult = OsmProcessor.execute(inputStream);
            printStats("Edited nodes count for users", processingResult.getUserToEditedNodesCount());
            printStats("Different nodes count containing key", processingResult.getKeyToMarkedNodesCount());
        } catch (FileNotFoundException e) {
            logger.error("File not found", e);
        } catch (IOException e) {
            logger.error("IO exception", e);
        }
    }

    private static void printStats(String title, Map<String, Integer> stats) {
        System.out.println(title);
        stats.entrySet().stream()
                .sorted((lhs, rhs) -> -(lhs.getValue() - rhs.getValue()))
                .forEach(it -> System.out.printf("%s: %d\n", it.getKey(), it.getValue()));
        System.out.println();
    }
}
