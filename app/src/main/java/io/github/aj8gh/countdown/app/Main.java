package io.github.aj8gh.countdown.app;

import io.github.aj8gh.countdown.app.cli.CountdownApp;
import io.github.aj8gh.countdown.generator.Generator;
import io.github.aj8gh.countdown.solver.Solver;

import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.INTERMEDIATE;
import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.SEQUENTIAL;
import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode;

public class Main {
    private static final CalculationMode SOLVE_MODE = INTERMEDIATE;
    private static final CalculationMode GEN_MODE = SEQUENTIAL;
    private static final long MODE_SWITCH_THRESHOLD = 50_000;
    private static final Generator GENERATOR = new Generator();
    private static final Solver SOLVER = new Solver();
    private static final CountdownApp APP = new CountdownApp(GENERATOR, SOLVER);

    public static void main(String... args) {
        setUp();
        APP.run();
    }

    private static void setUp() {
        SOLVER.setModeSwitchThreshold(MODE_SWITCH_THRESHOLD);
        GENERATOR.setMode(GEN_MODE);
        SOLVER.setMode(SOLVE_MODE);
    }
}
