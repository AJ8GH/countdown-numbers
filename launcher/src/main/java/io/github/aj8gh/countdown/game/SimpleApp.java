package io.github.aj8gh.countdown.game;

import io.github.aj8gh.countdown.BaseApp;
import io.github.aj8gh.countdown.gen.GenAdaptor;
import io.github.aj8gh.countdown.in.InputSupplier;
import io.github.aj8gh.countdown.out.OutputHandler;
import io.github.aj8gh.countdown.sol.SolAdaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class SimpleApp implements Consumer<String[]> {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleApp.class);
    private static final String SOL = "sol";
    private static final String GEN = "gen";

    private final OutputHandler outputHandler;
    private final InputSupplier inputSupplier;
    private final GenAdaptor generator;
    private final SolAdaptor solver;

    public SimpleApp(BaseApp baseApp) {
        this.outputHandler = baseApp.outputHandler();
        this.generator = baseApp.genAdaptor();
        this.solver = baseApp.solAdaptor();
        this.inputSupplier = baseApp.inputSupplier();
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
        input.forEach(i -> outputHandler.handleSolver(solver.solve(i)));
    }

    private void generate() {
        var input = inputSupplier.getGeneratorInput();
        input.forEach(i -> outputHandler.handleGenerator(generator.generate(i)));
    }
}
