package io.github.aj8gh.countdown.game;

import io.github.aj8gh.countdown.generator.Generator;
import io.github.aj8gh.countdown.solver.Solver;
import io.github.aj8gh.countdown.util.serialisation.Deserializer;
import io.github.aj8gh.countdown.util.serialisation.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.aj8gh.countdown.slack.SlackClient;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private static final Solver SOLVER = new Solver();
    private static final Generator GENERATOR = new Generator();
    private static final SlackClient CLIENT = new SlackClient();
    private static final Deserializer DESERIALIZER = new Deserializer();
    private static final Serializer SERIALIZER = new Serializer();

    private static final String SOL_IN = "sol.in";
    private static final String GEN_IN = "gen.in";
    private static final String DIR = "./input-output/";
    private static final String CHANNEL = "#bot-test";
    private static final int WARM_UPS = 40;

    public static void main(String[] args) {
        if (args.length == 0) {
            LOG.warn("Program needs sol.in or gen.in as a file arg");
            return;
        }
        handleInput(args[0]);
    }

    private static void handleInput(String fileHandle) {
        if (fileHandle.contains(SOL_IN)) {
            solve(fileHandle);
        } else if (fileHandle.contains(GEN_IN)) {
            generate(fileHandle);
        }
    }

    private static void solve(String file) {
        var input = DESERIALIZER.forSolver(DIR + file);
        SOLVER.warmUp(WARM_UPS);
        SOLVER.solve(input);
        serializeSolver();
        CLIENT.postMessage(CHANNEL, getSolverMessage());
    }

    private static void generate(String file) {
        var input = DESERIALIZER.forGenerator(DIR + file);
        GENERATOR.warmUp(WARM_UPS);
        GENERATOR.generate(input);
        serializeGenerator();
        CLIENT.postMessage(CHANNEL, getGeneratorMessage());
    }

    private static String getSolverMessage() {
        return String.format("Solution: %s = %s%nrpn: %s%ntime: %s%nattempts: %s",
                SOLVER.getSolution(), SOLVER.getSolution().getValue(), SOLVER.getSolution().getRpn(),
                SOLVER.getTime(), SOLVER.getAttempts());
    }

    private static String getGeneratorMessage() {
        return String.format("Question: %s%nMethod: %s = %s%nrpn: %s%ntime: %s%nattempts: %s",
                GENERATOR.getQuestionNumbers(), GENERATOR.getTarget(), GENERATOR.getTarget().getValue(),
                GENERATOR.getTarget().getRpn(), GENERATOR.getTime(), GENERATOR.getAttempts());
    }

    private static void serializeGenerator() {
        SERIALIZER.serializeGenerator(GENERATOR.getTarget().getRpn(),
                GENERATOR.getTarget().getValue(), GENERATOR.getTime());
    }

    private static void serializeSolver() {
        SERIALIZER.serializeSolver(SOLVER.getSolution().getRpn(), SOLVER.getTime());
    }
}
