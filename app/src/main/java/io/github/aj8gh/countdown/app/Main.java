package io.github.aj8gh.countdown.app;

import io.github.aj8gh.countdown.app.cli.CountdownApp;
import io.github.aj8gh.countdown.generator.Generator;
import io.github.aj8gh.countdown.solver.SimpleSolver;
import io.github.aj8gh.countdown.solver.SolutionCache;
import io.github.aj8gh.countdown.solver.Solver;
import io.github.aj8gh.countdown.util.calculator.Calculator;
import io.github.aj8gh.countdown.util.timer.Timer;

import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode;
import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.INTERMEDIATE;
import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.RUNNING;

public class Main {
    private static final CalculationMode SOLVE_MODE = RUNNING;
    private static final CalculationMode GEN_MODE = INTERMEDIATE;
    private static final int MODE_SWITCH_THRESHOLD = 20_000;
    private static final int WARM_UPS = 20;

    private static final Generator GENERATOR = new Generator(new Calculator(), new Timer(), 5);
    private static final Solver SOLVER = new SimpleSolver(
            new Calculator(), new SolutionCache(), new Timer());
    private static final CountdownApp APP = new CountdownApp(GENERATOR, SOLVER);

    static {
        SOLVER.setModeSwitchThreshold(MODE_SWITCH_THRESHOLD);
        warmUp();
    }

    public static void main(String... args) {
        APP.run();
    }

    private static void warmUp() {
        for (int i = 0; i < WARM_UPS; i++) {
            GENERATOR.generate(i % 5);
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