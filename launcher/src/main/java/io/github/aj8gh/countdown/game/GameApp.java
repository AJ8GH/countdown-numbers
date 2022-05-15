package io.github.aj8gh.countdown.game;

import io.github.aj8gh.countdown.BaseApp;
import io.github.aj8gh.countdown.gen.GenAdaptor;
import io.github.aj8gh.countdown.gen.GenResult;
import io.github.aj8gh.countdown.in.InputSupplier;
import io.github.aj8gh.countdown.out.OutputHandler;
import io.github.aj8gh.countdown.sol.SolAdaptor;
import io.github.aj8gh.countdown.sol.SolResult;
import io.github.aj8gh.countdown.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class GameApp implements Consumer<String[]> {
    private static final Logger LOG = LoggerFactory.getLogger(GameApp.class);
    private static final Random RANDOM = new Random();

    private final List<GenResult> genResults = new ArrayList<>();
    private final List<SolResult> solResults = new ArrayList<>();
    private final InputSupplier inputSupplier;
    private final OutputHandler outputHandler;
    private final GenAdaptor genAdaptor;
    private final SolAdaptor solAdaptor;

    private int runs;

    public GameApp(BaseApp baseApp, int runs) {
        this.solAdaptor = baseApp.solAdaptor();
        this.genAdaptor = baseApp.genAdaptor();
        this.outputHandler = baseApp.outputHandler();
        this.inputSupplier = baseApp.inputSupplier();
        this.runs = runs;
    }

    @Override
    public void accept(String... args) {
        runApp();
    }

    private void runApp() {
        LOG.info("Running game for {} balls", runs);
        createGenInputs();
        generate();
        solve();
    }

    private void createGenInputs() {
        LOG.info("Creating generator inputs");
        for (int i = 0; i < runs; i++) {
            outputHandler.handleGenInput(RANDOM.nextInt(5));
        }
    }

    private void generate() {
        LOG.info("Generating solver inputs");
        for (int genInput : inputSupplier.getGeneratorInput()) {
            outputHandler.handleGenerator(genAdaptor.generate(genInput));
            genResults.add(genAdaptor.getResult());
        }
        var time = reduceResults(genResults.stream().map(GenResult::getTime));
        LOG.info("Generator total time: {}, {} sets of numbers", time, genResults.size());
    }

    private void solve() {
        LOG.info("Solving...");
        for (List<Integer> solInput : inputSupplier.getSolverInput()) {
            outputHandler.handleSolver(solAdaptor.solve(solInput));
            solResults.add(solAdaptor.getResult());
            if (solAdaptor.getExtraNumbers() > 0) {
                runs -= solAdaptor.getExtraNumbers();
                LOG.info("{} balls down, {} balls left after {} solves!",
                        solAdaptor.getExtraNumbers(), Math.max(0, runs), solResults.size());
            }
            if (runs <= 0) {
                LOG.info("Generator balled out in {} solves!", solResults.size());
                break;
            }
        }
        var time = reduceResults(solResults.stream().map(SolResult::getTime));
        LOG.info("Solver total time: {}, {} solves", time, solResults.size());
    }

    private double reduceResults(Stream<Double> times) {
        return times.reduce(0.0, Double::sum);
    }
}
