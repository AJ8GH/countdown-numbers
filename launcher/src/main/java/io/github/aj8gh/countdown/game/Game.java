package io.github.aj8gh.countdown.game;

import io.github.aj8gh.countdown.gen.Generator;
import io.github.aj8gh.countdown.in.InputSupplier;
import io.github.aj8gh.countdown.out.OutputHandler;
import io.github.aj8gh.countdown.sol.Solver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class Game implements Consumer<String[]> {
    private static final Logger LOG = LoggerFactory.getLogger(Game.class);
    private static final String SOL = "sol";
    private static final String GEN = "gen";

    private final OutputHandler outputHandler;
    private final InputSupplier inputSupplier;
    private final Generator generator;
    private final Solver solver;

    public Game(OutputHandler outputHandler,
                InputSupplier inputSupplier,
                Generator generator,
                Solver solver) {
        this.outputHandler = outputHandler;
        this.inputSupplier = inputSupplier;
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
            solve();
        } else if (input.contains(GEN)) {
            generate();
        }
    }

    private void solve() {
        var input = inputSupplier.getSolverInput();
        solver.warmUp();
        solver.solve(input);
        outputHandler.handleSolver(solver);
    }

    private void generate() {
        var input = inputSupplier.getGeneratorInput();
        generator.warmUp();
        generator.generate(input);
        outputHandler.handleGenerator(generator);
    }
}
