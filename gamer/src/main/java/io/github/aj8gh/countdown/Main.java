package io.github.aj8gh.countdown;

import io.github.aj8gh.countdown.conf.AppConfig;
import io.github.aj8gh.countdown.game.Gamer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private static final AppConfig APP_CONFIG = new AppConfig();
    private static final String SOL = "sol";
    private static final String GEN = "gen";

    private static final Gamer GAMER = APP_CONFIG.gamer();
    private static String inputFile;
    private static String outputFile;

    public static void main(String[] args) {
        LOG.info("*** jonasa Countdown app running ***");
        inputFile = args[0];
        outputFile = args[1];
        if (inputFile.contains(GEN)) {
            runGenerator();
        } else if (inputFile.contains(SOL)) {
            runSolver();
        }
    }

    private static void runGenerator() {
        GAMER.tailGenFile(inputFile, outputFile);
    }

    private static void runSolver() {
        GAMER.tailSolFile(inputFile, outputFile);
    }
}
