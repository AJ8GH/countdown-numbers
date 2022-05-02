package io.github.aj8gh.countdown.out.serial;

import io.github.aj8gh.countdown.gen.Generator;
import io.github.aj8gh.countdown.out.OutputHandler;
import io.github.aj8gh.countdown.sol.Solver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import static io.github.aj8gh.countdown.out.OutputHandler.OutputType.FILE;

public class Serializer implements OutputHandler {
    private static final OutputType TYPE = FILE;
    private static final Logger LOG = LoggerFactory.getLogger(Serializer.class);
    private static final String COMMA = ",";
    private static final String COLON = ":";

    private final String ioDir;
    private final String genOutFile;
    private final String solOutFile;
    private final String solInFile;

    private boolean createSolverInput;

    public Serializer(String ioDir, String genOutFile, String solOutFile, String solInFile) {
        this.ioDir = ioDir;
        this.genOutFile = genOutFile;
        this.solOutFile = solOutFile;
        this.solInFile = solInFile;
    }

    @Override
    public void handleGenerator(Generator generator) {
        var filePath = buildFilePath(genOutFile);
        var data = generator.getQuestionNumbers() + COLON + generator.getTarget() + COLON + generator.getTime();
        serialize(filePath, data);
        if (createSolverInput) {
            createSolverInput(generator.getQuestionNumbers());
        }
    }

    @Override
    public void handleSolver(Solver solver) {
        var filePath = buildFilePath(solOutFile);
        var data = solver.getSolution() + COLON + solver.getTime();
        serialize(filePath, data);
    }

    private void createSolverInput(List<Integer> question) {
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

    public OutputType getType() {
        return TYPE;
    }

    public void setCreateSolverInput(boolean createSolverInput) {
        this.createSolverInput = createSolverInput;
    }
}
