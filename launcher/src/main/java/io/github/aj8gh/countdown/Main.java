package io.github.aj8gh.countdown;

import io.github.aj8gh.countdown.util.calculator.Calculator;
import io.github.aj8gh.countdown.util.timer.Timer;
import io.github.aj8gh.countdown.generator.Generator;
import io.github.aj8gh.countdown.solver.SimpleSolver;
import io.github.aj8gh.countdown.solver.Solver;
import io.github.aj8gh.countdown.util.CountdownLogger;
import io.github.aj8gh.countdown.util.TestInputs;

import java.util.ArrayList;
import java.util.List;

import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.INTERMEDIATE;
import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.RUNNING;
import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode;

public class Main {
    private static final CountdownLogger LOG = new CountdownLogger();
    private static final int NUMBER_OF_LARGE = 2;
    private static final int RUNS = 10;
    private static final int WARM_UPS = 0;

    private static final Generator GENERATOR = new Generator(new Calculator(), new Timer(), 5);
    private static final Solver SOLVER = new SimpleSolver(new Calculator(), new Timer());

    private static final CalculationMode SOLVE_MODE = RUNNING;
    private static final CalculationMode GEN_MODE = INTERMEDIATE;


    static {
        GENERATOR.setMode(GEN_MODE);
        SOLVER.setMode(SOLVE_MODE);
        SOLVER.setModeSwitchThreshold(20_000);
        warmUp();
    }

    public static void main(String... args) {
        runTestInputs();
        LOG.logTime("\n*** Total time: {} ***\n", SOLVER.getTotalTime(), 3);
    }

    private static void runTestInputs() {
        TestInputs.TRICKY.forEach(Main::solve);
    }

    private static void runSolver() {
        for (int i = 0; i < RUNS; i++) {
            var target = GENERATOR.generateTarget(NUMBER_OF_LARGE);
            var solution = SOLVER.solve(GENERATOR.getQuestionNumbers());
            LOG.logQuestionAndSolution(target, solution, GENERATOR, SOLVER);
            reset();
        }
    }

    private static void solve(List<Integer> input) {
        var solution = SOLVER.solve(new ArrayList<>(input));
        LOG.logSolution(input, solution, SOLVER);
        reset();
    }

    private static void warmUp() {
        for (int i = 0; i < WARM_UPS; i++) {
            GENERATOR.generateTarget(NUMBER_OF_LARGE);
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
