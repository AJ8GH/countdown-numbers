package io.github.aj8gh.countdown.game;

import io.github.aj8gh.countdown.conf.AppConfig;
import io.github.aj8gh.countdown.gen.Generator;
import io.github.aj8gh.countdown.out.OutputHandler;
import io.github.aj8gh.countdown.out.serial.Deserializer;
import io.github.aj8gh.countdown.sol.Solver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private static final OutputHandler OUTPUT_HANDLER = AppConfig.outputHandler();
    private static final Deserializer DESERIALIZER = AppConfig.deserializer();
    private static final Generator GENERATOR = AppConfig.generator();
    private static final Solver SOLVER = AppConfig.solver();
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
        OUTPUT_HANDLER.handleSolver(SOLVER);
    }

    private static void generate(String file) {
        var input = DESERIALIZER.forGenerator(file);
        GENERATOR.warmUp();
        GENERATOR.generate(input);
        OUTPUT_HANDLER.handleGenerator(GENERATOR);
    }
}
