package io.github.aj8gh.countdown.benchmarking;

import io.github.aj8gh.countdown.generator.Generator;
import io.github.aj8gh.countdown.solver.SimpleSolver;
import io.github.aj8gh.countdown.solver.Solver;
import io.github.aj8gh.countdown.util.calculator.Calculator;
import io.github.aj8gh.countdown.util.timer.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode;
import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.INTERMEDIATE;
import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.RUNNING;

public class Tester {
    private static final Logger LOG = LoggerFactory.getLogger(Tester.class);
    private static final CalculationMode SOLVE_MODE = RUNNING;
    private static final CalculationMode GEN_MODE = INTERMEDIATE;
    private static final int NUMBER_OF_LARGE = 2;
    private static final int WARM_UPS = 20;
    private static final int RUNS = 10;

    private static final Generator GENERATOR = new Generator(new Calculator(), new Timer(), 5);
    private static final Solver SOLVER = new SimpleSolver(new Calculator(), new Timer());

    static {
        GENERATOR.setMode(GEN_MODE);
        SOLVER.setMode(SOLVE_MODE);
        SOLVER.setModeSwitchThreshold(20_000);
        warmUp();
    }

    public static void main(String... args) {
        runTestInputs();
    }

    private static void runTestInputs() {
        TestInputs.TRICKY.forEach(Tester::solve);
    }

    private static void runSolver() {
        for (int i = 0; i < RUNS; i++) {
            GENERATOR.generate(NUMBER_OF_LARGE);
            SOLVER.solve(GENERATOR.getQuestionNumbers());
            LOG.info("{}", GENERATOR);
            LOG.info("{}", SOLVER);
            reset();
        }
    }

    private static void solve(List<Integer> input) {
        SOLVER.solve(new ArrayList<>(input));
        LOG.info("{}", SOLVER);
        reset();
    }

    private static void warmUp() {
        for (int i = 0; i < WARM_UPS; i++) {
            GENERATOR.generate(NUMBER_OF_LARGE);
            SOLVER.solve(GENERATOR.getQuestionNumbers());
            reset();
        }
    }

    private static void reset() {
        GENERATOR.reset();
        SOLVER.reset();
        GENERATOR.setMode(GEN_MODE);
        SOLVER.setMode(SOLVE_MODE);
    }
}
