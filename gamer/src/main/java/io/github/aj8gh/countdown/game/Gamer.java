package io.github.aj8gh.countdown.game;

import io.github.aj8gh.countdown.gen.GenAdaptor;
import io.github.aj8gh.countdown.sol.SolAdaptor;
import io.github.aj8gh.countdown.util.RpnConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toList;

public class Gamer {
    private static final Logger LOG = LoggerFactory.getLogger(Gamer.class);
    private static final RpnConverter RPN_CONVERTER = new RpnConverter();
    private static final String COMMA = ",";
    private static final String COLON = ":";

    private final ScheduledExecutorService scheduler;
    private final Serializer serializer;
    private final GenAdaptor generator;
    private final SolAdaptor solver;
    private final Timer timer;

    private File inputFile;
    private String outputFile;
    private int readInterval;
    private long offset;

    public Gamer(GamerBuilder builder) {
        this.serializer = builder.getSerializer();
        this.scheduler = builder.getScheduler();
        this.generator = builder.getGenerator();
        this.solver = builder.getSolver();
        this.timer = builder.getTimer();
    }

    public void runGenerator(String inputFile, String outputFile) {
        this.outputFile = outputFile;
        this.inputFile = new File(inputFile);
        tail(generator::warmup, this::generate);
    }

    public void runSolver(String inputFile, String outputFile) {
        this.outputFile = outputFile;
        this.inputFile = new File(inputFile);
        tail(solver::warmup, this::solve);
    }

    public void tail(Runnable warmup, Consumer<String> handler) {
        LOG.info("*** Tailing {} ***", inputFile);
        scheduler.scheduleAtFixedRate(() -> {
            if (inputFile.length() > offset) {
                warmup.run();
                deserialize(handler);
            }
        }, readInterval, readInterval, TimeUnit.MILLISECONDS);
    }

    private void deserialize(Consumer<String> handler) {
        try (var randomAccessFile = new RandomAccessFile(inputFile, "r")) {
            randomAccessFile.seek(offset);
            String input;
            while ((input = randomAccessFile.readLine()) != null) {
                timer.start();
                handler.accept(input);
                timer.reset();
            }
            offset = randomAccessFile.getFilePointer();
        } catch (IOException e) {
            handleException(inputFile, e);
        }
    }

    private void handleException(File file, Exception e) {
        LOG.error("Error whilst tailing file {}", file, e);
        if (e instanceof InterruptedException) {
            Thread.currentThread().interrupt();
        }
    }

    private void generate(String numLarge) {
        var result = generator.generate(Integer.parseInt(numLarge));
        var rpn = RPN_CONVERTER.convert(result.toString());
        timer.stop();
        serializer.serializeGenerator(rpn, result.getValue(), timer.getTime(), outputFile);
    }

    private void solve(String numbers) {
        var questionAndTarget = numbers.split(COLON);
        var question = Arrays.stream(questionAndTarget[0].split(COMMA))
                .map(Integer::valueOf)
                .collect(toList());
        question.add(Integer.valueOf(questionAndTarget[1]));
        var rpn = RPN_CONVERTER.convert(solver.solve(question).toString());
        timer.stop();
        serializer.serializeSolver(rpn, timer.getTime(), outputFile);
    }

    public void setReadInterval(int readInterval) {
        this.readInterval = readInterval;
    }
}
