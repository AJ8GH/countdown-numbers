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
    private static final int DEFAULT_BALLS = 6;

    private final List<GenResult> genResults = new ArrayList<>();
    private final List<SolResult> solResults = new ArrayList<>();
    private final int runs;
    private int balls = DEFAULT_BALLS;

    private final SolAdaptor solAdaptor;
    private final GenAdaptor genAdaptor;
    private final OutputHandler outputHandler;
    private final InputSupplier inputSupplier;


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
        createGenInputs();
        generate();
        solve();
    }

    private void createGenInputs() {
        for (int i = 0; i < runs; i++) {
            outputHandler.handleGenInput(RANDOM.nextInt(5));
        }
    }

    private void generate() {
        for (int genInput : inputSupplier.getGeneratorInput()) {
            outputHandler.handleGenerator(genAdaptor.generate(genInput));
            genResults.add(genAdaptor.getResult());
        }
        var time = reduceResults(genResults.stream().map(GenResult::getTime));
        LOG.info("Generator total time: {}", time);
    }

    private void solve() {
        for (List<Integer> solInput : inputSupplier.getSolverInput()) {
            outputHandler.handleSolver(solAdaptor.solve(solInput));
            solResults.add(solAdaptor.getResult());
            if (solAdaptor.getExtraNumbers() > 0) {
                balls -= solAdaptor.getExtraNumbers();
                LOG.info("{} balls down after {} solves!", solAdaptor.getExtraNumbers(), solResults.size());
            }
            if (balls <= 0) {
                LOG.info("Generator balled out in {} solves!", solResults.size());
                break;
            }
        }
        var time = reduceResults(solResults.stream().map(SolResult::getTime));
        LOG.info("Solver total time: {}", time);

    }

    private double reduceResults(Stream<Double> times) {
        return times.reduce(0.0, Double::sum);
    }

    public void setBalls(int balls) {
        this.balls = balls;
    }
}
