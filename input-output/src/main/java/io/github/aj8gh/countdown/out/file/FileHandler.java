package io.github.aj8gh.countdown.out.file;

import io.github.aj8gh.countdown.gen.Generator;
import io.github.aj8gh.countdown.out.OutputHandler;
import io.github.aj8gh.countdown.sol.Solver;
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
    public void handleSolver(Solver solver) {
        serializer.serializeSolver(solver, buildFilePath(solOutFile));
    }

    @Override
    public void handleGenerator(Generator generator) {
        serializer.serializeGenerator(generator, buildFilePath(genOutFile));
        if (createSolverInput) {
            var file = buildFilePath(solInFile);
            serializer.createSolverInput(generator.getQuestionNumbers(), file);
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
