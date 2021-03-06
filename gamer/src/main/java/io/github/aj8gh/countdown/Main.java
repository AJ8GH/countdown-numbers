package io.github.aj8gh.countdown;

import io.github.aj8gh.countdown.conf.AppConfig;
import io.github.aj8gh.countdown.game.Gamer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private static final AppConfig APP_CONFIG = new AppConfig();
    private static final Gamer GAMER = APP_CONFIG.gamer();
    private static final String SOL = "sol";
    private static final String GEN = "gen";

    private static String inputFile;
    private static String outputFile;

    public static void main(String... args) {
        LOG.info("*** Running Countdown App ***");
        parse(args);
        if (inputFile.contains(GEN)) {
            GAMER.runGenerator(inputFile, outputFile);
        } else if (inputFile.contains(SOL)) {
            GAMER.runSolver(inputFile, outputFile);
        }
    }

    private static void parse(String... args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("Needs 2 args");
        }
        inputFile = args[0];
        outputFile = args[1];
    }
}
