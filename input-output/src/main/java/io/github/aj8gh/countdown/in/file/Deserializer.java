package io.github.aj8gh.countdown.in.file;

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
        this.solInFile = buildFilePath(solInFile);
        this.genInFile = buildFilePath(genInFile);
    }

    public List<Integer> forGenerator() {
        LOG.info("*** Reading from {} ***", genInFile);
        try (var reader = new Scanner(new FileReader(genInFile))) {
            List<Integer> inputs = new ArrayList<>();
            while (reader.hasNextInt()) {
                inputs.add(reader.nextInt());
            }
            return inputs;
        } catch (IOException e) {
            LOG.error("Error reading generator input file {}, {}", genInFile, e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<List<Integer>> forSolver() {
        LOG.info("*** Reading from {} ***", solInFile);
        try (var reader = new Scanner(new FileReader(solInFile))) {
            List<List<Integer>> inputs = new ArrayList<>();
            while (reader.hasNextLine()) {
                var line = reader.nextLine();
                var questionAndTarget = line.split(COLON);
                var question = new ArrayList<>(Arrays
                        .stream(questionAndTarget[0].split(COMMA))
                        .map(Integer::valueOf)
                        .toList());
                question.add(Integer.valueOf(questionAndTarget[1]));
                inputs.add(question);
            }
            return inputs;
        } catch (IOException e) {
            LOG.error("Error reading solver input file {}, {}", solInFile, e.getMessage());
            return Collections.emptyList();
        }
    }

    private String buildFilePath(String file) {
        var filePath = ioDir + file;
        if (!new File(filePath).exists()) {
            throw new IllegalArgumentException("File does not exist " + filePath);
        }
        return filePath;
    }
}
