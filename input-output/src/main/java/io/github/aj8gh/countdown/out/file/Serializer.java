package io.github.aj8gh.countdown.out.file;

import io.github.aj8gh.countdown.gen.Generator;
import io.github.aj8gh.countdown.sol.Solver;
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

    public void serializeGenerator(Generator generator, String file) {
        var data = generator.getQuestionNumbers().toString() +
                COLON + generator.getTarget() +
                COLON + generator.getTime();
        serialize(file, data);

    }

    public void serializeSolver(Solver solver, String file) {
        var data = solver.getSolution() + COLON + solver.getTime();
        serialize(file, data);
    }

    void createSolverInput(List<Integer> question, String file) {
        var target = question.remove(question.size() - 1);
        var numberString = String.join(COMMA, question.stream()
                .map(String::valueOf)
                .toList());
        var data = numberString + COLON + target;
        serialize(file, data);
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
