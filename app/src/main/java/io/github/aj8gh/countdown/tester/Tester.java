package io.github.aj8gh.countdown.tester;

import io.github.aj8gh.countdown.app.cli.Shell;
import io.github.aj8gh.countdown.config.AppConfig;
import io.github.aj8gh.countdown.solver.Solver;

import java.util.List;

public class Tester {
    private static final Solver SOLVER = AppConfig.solver();
    private static final Shell SHELL = new Shell(AppConfig.serializer());

    public static void main(String... args) {
        solve();
    }

    private static void solve() {
        SOLVER.warmUp();
        SOLVER.solve(List.of(25, 100, 50, 5, 6, 6, 236));
        SHELL.logSolver(SOLVER);
    }
}
