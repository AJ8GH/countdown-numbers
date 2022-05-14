package io.github.aj8gh.countdown.out.file;

import io.github.aj8gh.countdown.gen.GenResult;
import io.github.aj8gh.countdown.out.OutputHandler;
import io.github.aj8gh.countdown.sol.SolResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;

import static io.github.aj8gh.countdown.out.OutputHandler.OutputType.FILE;

public class FileHandler implements OutputHandler {
    private static final Logger LOG = LoggerFactory.getLogger(FileHandler.class);
    private static final OutputType TYPE = FILE;

    private final Serializer serializer;
    private boolean createSolverInput;
    private String ioDir;
    private String genInFile;
    private String genOutFile;
    private String solInFile;
    private String solOutFile;

    public FileHandler(Serializer serializer, FileProvider files, boolean clearFiles) {
        this.serializer = serializer;
        this.ioDir = files.getIoDir();
        this.genInFile = files.getGenInFile();
        this.genOutFile = files.getGenOutFile();
        this.solInFile = files.getSolInFile();
        this.solOutFile = files.getSolOutFile();
        if (clearFiles) clearFiles();
    }

    @Override
    public void handleSolver(SolResult result) {
        serializer.serializeSolver(result, buildFilePath(solOutFile));
    }

    @Override
    public void handleGenerator(GenResult genResult) {
        serializer.serializeGenerator(genResult, buildFilePath(genOutFile));
        if (createSolverInput) {
            var file = buildFilePath(solInFile);
            serializer.createSolverInput(genResult.getQuestionNumbers(), file);
        }
    }

    @Override
    public void handleGenInput(int numLarge) {
        var filePath = buildFilePath(genInFile);
        serializer.createGeneratorInput(filePath, String.valueOf(numLarge));
    }


    private String buildFilePath(String file) {
        return ioDir + file;
    }

    private void clearFiles() {
        clearFile(buildFilePath(genOutFile));
        clearFile(buildFilePath(genInFile));
        clearFile(buildFilePath(solInFile));
        clearFile(buildFilePath(solOutFile));
    }

    private void clearFile(String filePath) {
        try (var pw = new PrintWriter(filePath)) {
            LOG.info("File {} cleared", filePath);
        } catch (IOException e) {
            LOG.error("Error clearing file {}", filePath);
        }
    }

    public void setCreateSolverInput(boolean createSolverInput) {
        this.createSolverInput = createSolverInput;
    }

    public OutputType getType() {
        return TYPE;
    }
}
