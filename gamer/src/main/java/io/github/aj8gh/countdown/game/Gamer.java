package io.github.aj8gh.countdown.game;

import io.github.aj8gh.countdown.gen.GenAdaptor;
import io.github.aj8gh.countdown.sol.SolAdaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toList;

public class Gamer {
    private static final Logger LOG = LoggerFactory.getLogger(Gamer.class);
    private static final String COMMA = ",";
    private static final String COLON = ":";

    private final GenAdaptor generator;
    private final SolAdaptor solver;
    private final Timer timer;
    private final Serializer serializer;

    private String genOutFilePath;
    private String solOutFilePath;
    private boolean tail = true;
    private int readIntervalMillis = 100;
    private int warmUps;

    public Gamer(GenAdaptor generator, SolAdaptor solver,
                 Timer timer, Serializer serializer) {
        this.generator = generator;
        this.solver = solver;
        this.timer = timer;
        this.serializer = serializer;
    }

    public void tailGenFile(String genInFilePath, String genOutFilePath) {
        this.genOutFilePath = genOutFilePath;
        tail(new File(genInFilePath), this::generate);
    }

    public void tailSolFile(String solInFilePath, String solOutFilePath) {
        this.solOutFilePath = solOutFilePath;
        tail(new File(solInFilePath), this::solve);
    }

    public void tail(File file, Consumer<String> handler) {
        try {
            LOG.info("*** Tailing {} ***", file);
            long offset = 0;
                while (tail) {
                    Thread.sleep(readIntervalMillis);
                    var fileLength = file.length();
                    if (fileLength > offset) {
                        try (var randomAccessFile = new RandomAccessFile(file, "r")) {
                            randomAccessFile.seek(offset);
                            warmUp();
                            String input;
                            while ((input = randomAccessFile.readLine()) != null) {
                                timer.start();
                                handler.accept(input);
                                timer.reset();
                            }
                            offset = randomAccessFile.getFilePointer();
                        }
                    }
                }
        } catch (Exception e) {
            LOG.error("Error reading generator input file {}", file, e);
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void generate(String numLarge) {
        generator.generate(Integer.parseInt(numLarge));
        timer.stop();
        serializer.serializeGenerator(generator.getSolution(), timer.getTime(), genOutFilePath);
    }

    private void solve(String numbers) {
        var questionAndTarget = numbers.split(COLON);
        var question = Arrays.stream(questionAndTarget[0].split(COMMA))
                .map(Integer::valueOf)
                .collect(toList());
        question.add(Integer.valueOf(questionAndTarget[1]));
        solver.solve(question);
        timer.stop();
        serializer.serializeSolver(solver.getSolution(), timer.getTime(), solOutFilePath);
    }

    private void warmUp() {
        for (int i = 0; i < warmUps; i++) {
            generator.generate(i % 5);
            solver.solve(generator.getQuestionNumbers());
        }
    }

    public void setReadIntervalMillis(int readIntervalMillis) {
        this.readIntervalMillis = readIntervalMillis;
    }

    public void setTail(boolean tail) {
        this.tail = tail;
    }

    public void setWarmUps(int warmUps) {
        this.warmUps = warmUps;
    }
}
