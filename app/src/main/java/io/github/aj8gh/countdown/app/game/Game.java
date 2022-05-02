package io.github.aj8gh.countdown.app.game;

import io.github.aj8gh.countdown.gen.Generator;
import io.github.aj8gh.countdown.out.OutputHandler;
import io.github.aj8gh.countdown.out.file.Deserializer;
import io.github.aj8gh.countdown.sol.Solver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class Game implements Consumer<String[]> {
    private static final Logger LOG = LoggerFactory.getLogger(Game.class);
    private static final String SOL = "sol";
    private static final String GEN = "gen";

    private final OutputHandler outputHandler;
    private final Deserializer deserializer;
    private final Generator generator;
    private final Solver solver;

    public Game(OutputHandler outputHandler,
                Deserializer deserializer,
                Generator generator,
                Solver solver) {
        this.outputHandler = outputHandler;
        this.deserializer = deserializer;
        this.generator = generator;
        this.solver = solver;
    }

    @Override
    public void accept(String... args) {
        if (args.length == 0) {
            LOG.warn("Program needs sol, gen or a file arg");
            return;
        }
        handleInput(args[0]);
    }

    private void handleInput(String input) {
        if (input.contains(SOL)) {
            solve(input);
        } else if (input.contains(GEN)) {
            generate(input);
        }
    }

    private void solve(String file) {
        var input = deserializer.forSolver(file);
        solver.warmUp();
        solver.solve(input);
        outputHandler.handleSolver(solver);
    }

    private void generate(String file) {
        var input = deserializer.forGenerator(file);
        generator.warmUp();
        generator.generate(input);
        outputHandler.handleGenerator(generator);
    }
}
