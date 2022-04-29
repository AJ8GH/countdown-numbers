package io.github.aj8gh.countdown.game;

import io.github.aj8gh.countdown.config.AppConfig;
import io.github.aj8gh.countdown.generator.Generator;
import io.github.aj8gh.countdown.slack.SlackClient;
import io.github.aj8gh.countdown.solver.Solver;
import io.github.aj8gh.countdown.util.serialisation.Deserializer;
import io.github.aj8gh.countdown.util.serialisation.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private static final Solver SOLVER = AppConfig.solver();
    private static final Generator GENERATOR = AppConfig.generator();
    private static final SlackClient CLIENT = AppConfig.slackClient();
    private static final Deserializer DESERIALIZER = AppConfig.deserializer();
    private static final Serializer SERIALIZER = AppConfig.serializer();
    private static final String SOL = "sol";
    private static final String GEN = "gen";

    public static void main(String[] args) {
        if (args.length == 0) {
            LOG.warn("Program needs sol, gen or a file arg");
            return;
        }
        handleInput(args[0]);
    }

    private static void handleInput(String input) {
        if (input.contains(SOL)) {
            solve(input);
        } else if (input.contains(GEN)) {
            generate(input);
        }
    }

    private static void solve(String file) {
        var input = DESERIALIZER.forSolver(file);
        SOLVER.warmUp();
        SOLVER.solve(input);
        serializeSolver();
        CLIENT.postMessage(getSolverMessage());
    }

    private static void generate(String file) {
        var input = DESERIALIZER.forGenerator(file);
        GENERATOR.warmUp();
        GENERATOR.generate(input);
        serializeGenerator();
        CLIENT.postMessage(getGeneratorMessage());
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
