package io.github.aj8gh.countdown.out.file;

import io.github.aj8gh.countdown.gen.GenResult;
import io.github.aj8gh.countdown.out.OutputHandler;
import io.github.aj8gh.countdown.sol.SolResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.aj8gh.countdown.out.OutputHandler.OutputType.FILE;

public class FileHandler implements OutputHandler {
    private static final Logger LOG = LoggerFactory.getLogger(FileHandler.class);
    private static final OutputType TYPE = FILE;

    private final Serializer serializer;
    private boolean createSolverInput;
    private String ioDir;
    private String genOutFile;
    private String solOutFile;
    private String solInFile;

    public FileHandler(Serializer serializer) {
        this.serializer = serializer;
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

    private String buildFilePath(String file) {
        var filePath = ioDir + file;
        LOG.info("*** Writing to {} ***", file);
        return filePath;
    }

    public void setCreateSolverInput(boolean createSolverInput) {
        this.createSolverInput = createSolverInput;
    }

    public void setIoDir(String ioDir) {
        this.ioDir = ioDir;
    }

    public void setGenOutFile(String genOutFile) {
        this.genOutFile = genOutFile;
    }

    public void setSolOutFile(String solOutFile) {
        this.solOutFile = solOutFile;
    }

    public void setSolInFile(String solInFile) {
        this.solInFile = solInFile;
    }

    public OutputType getType() {
        return TYPE;
    }
}
