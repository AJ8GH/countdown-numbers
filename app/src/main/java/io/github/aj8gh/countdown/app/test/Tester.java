package io.github.aj8gh.countdown.app.test;

import io.github.aj8gh.countdown.app.conf.AppConfig;
import io.github.aj8gh.countdown.out.OutputHandler;
import io.github.aj8gh.countdown.sol.Solver;
import io.github.aj8gh.countdown.in.InputProvider;

public class Tester {
    private static final Solver SOLVER = AppConfig.solver();
    private static final OutputHandler OUTPUT_HANDLER = AppConfig.outputHandler();
    private static final InputProvider INPUT_PROVIDER = AppConfig.inputProvider();

    public static void main(String... args) {
        solve();
    }

    private static void solve() {
        SOLVER.warmUp();
        SOLVER.solve(INPUT_PROVIDER.testInput());
        OUTPUT_HANDLER.handleSolver(SOLVER);
    }
}
