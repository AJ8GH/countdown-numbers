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

    private static final String IO_DIR = "./input-output/";
    private static final String GEN_OUT_HANDLE = IO_DIR + "gen.out";
    private static final String SOL_OUT_HANDLE = IO_DIR + "sol.out";
    private static final String SOL_IN_HANDLE = IO_DIR + "sol.in";

    private static final String COMMA = ",";
    private static final String COLON = ":";

    public void serializeGenerator(String question, int target, double time) {
        var data = question + COLON + target + COLON + time;
        serialize(GEN_OUT_HANDLE, data);
    }

    public void serializeSolver(String solution, double time) {
        var data = solution + COLON + time;
        serialize(SOL_OUT_HANDLE, data);
    }

    public void createSolverInput(List<Integer> question) {
        var target = question.remove(question.size() - 1);
        var numberString = String.join(COMMA, question.stream().map(String::valueOf).toList());
        var data = numberString + COLON + target;
        serialize(SOL_IN_HANDLE, data);
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
}
