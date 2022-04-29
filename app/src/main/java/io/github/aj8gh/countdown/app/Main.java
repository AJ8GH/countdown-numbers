package io.github.aj8gh.countdown.app;

import io.github.aj8gh.countdown.app.cli.CountdownApp;
import io.github.aj8gh.countdown.app.cli.Shell;
import io.github.aj8gh.countdown.config.AppConfig;
import io.github.aj8gh.countdown.generator.Generator;
import io.github.aj8gh.countdown.solver.Solver;

public class Main {
    private static final Generator GENERATOR = AppConfig.generator();
    private static final Solver SOLVER = AppConfig.solver();
    private static final Shell SHELL = new Shell(AppConfig.serializer());

    private static final CountdownApp APP = new CountdownApp(SHELL, GENERATOR, SOLVER);

    public static void main(String... args) {
        APP.run();
    }
}
