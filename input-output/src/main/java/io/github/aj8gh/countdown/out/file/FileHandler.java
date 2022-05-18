package io.github.aj8gh.countdown.out.file;

import io.github.aj8gh.countdown.gen.GenResult;
import io.github.aj8gh.countdown.out.OutputHandler;
import io.github.aj8gh.countdown.sol.SolResult;

public class FileHandler implements OutputHandler {
    private final Serializer serializer;
    private final String genInFile;
    private final String genOutFile;
    private final String solOutFile;

    public FileHandler(Serializer serializer, FileProvider files) {
        this.serializer = serializer;
        this.genInFile = files.getGenInFile();
        this.genOutFile = files.getGenOutFile();
        this.solOutFile = files.getSolOutFile();
    }

    @Override
    public void handleSolver(SolResult result) {
        serializer.serializeSolver(result, solOutFile);
    }

    @Override
    public void handleGenerator(GenResult genResult) {
        serializer.serializeGenerator(genResult, genOutFile);
    }

    @Override
    public void handleGenInput(int numLarge) {
        serializer.createGeneratorInput(genInFile, String.valueOf(numLarge));
    }
}
