package io.github.aj8gh.countdown.util.serialisation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class Serializer {
    private static final Logger LOG = LoggerFactory.getLogger(Serializer.class);
    private static final String COMMA = ",";
    private static final String COLON = ":";

    private final String ioDir;
    private final String genOutFile;
    private final String solOutFile;
    private final String solInFile;

    public Serializer(String ioDir, String genOutFile, String solOutFile, String solInFile) {
        this.ioDir = ioDir;
        this.genOutFile = genOutFile;
        this.solOutFile = solOutFile;
        this.solInFile = solInFile;
    }

    public void serializeGenerator(String question, int target, double time) {
        var filePath = buildFilePath(genOutFile);
        var data = question + COLON + target + COLON + time;
        serialize(filePath, data);
    }

    public void serializeSolver(String solution, double time) {
        var filePath = buildFilePath(solOutFile);
        var data = solution + COLON + time;
        serialize(filePath, data);
    }

    public void createSolverInput(List<Integer> question) {
        var filePath = buildFilePath(solInFile);
        var target = question.remove(question.size() - 1);
        var numberString = String.join(COMMA, question.stream()
                .map(String::valueOf)
                .toList());
        var data = numberString + COLON + target;
        serialize(filePath, data);
    }

    private void serialize(String file, String data) {
        try (var writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file)))) {
            writer.write(data);
            writer.flush();
        } catch (IOException e) {
            LOG.error("Error writing {} to file {}, {}",
                    data, file, e.getMessage());
        }
    }

    private String buildFilePath(String file) {
        var filePath = ioDir + file;
        LOG.info("*** Writing to {} ***", file);
        return filePath;
    }
}
