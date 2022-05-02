package io.github.aj8gh.countdown.out.serial;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Deserializer {
    private static final Logger LOG = LoggerFactory.getLogger(Deserializer.class);
    private static final String COMMA = ",";
    private static final String COLON = ":";

    private final String ioDir;
    private final String solInFile;
    private final String genInFile;

    public Deserializer(String ioDir, String solInFile, String genInFile) {
        this.ioDir = ioDir;
        this.solInFile = solInFile;
        this.genInFile = genInFile;
    }

    public int forGenerator(String file) {
        var filePath = buildFilePath(file, genInFile);
        try (var reader = new Scanner(new FileReader(filePath))) {
            return reader.nextInt();
        } catch (IOException e) {
            LOG.error("Error reading generator input file {}, {}", filePath, e.getMessage());
            return -1;
        }
    }

    public List<Integer> forSolver(String file) {
        var filePath = buildFilePath(file, solInFile);
        try (var reader = new Scanner(new FileReader(filePath))) {
            var line = reader.nextLine();
            var questionAndTarget = line.split(COLON);
            var question = new ArrayList<>(Arrays
                    .stream(questionAndTarget[0].split(COMMA))
                    .map(Integer::valueOf)
                    .toList());
            question.add(Integer.valueOf(questionAndTarget[1]));
            return question;
        } catch (IOException e) {
            LOG.error("Error reading solver input file {}, {}", filePath, e.getMessage());
            return Collections.emptyList();
        }
    }

    private String buildFilePath(String file, String defaultFile) {
        var filePath = ioDir + (new File(ioDir + file).exists() ? file : defaultFile);
        LOG.info("*** Reading from {} ***", filePath);
        return filePath;
    }
}
