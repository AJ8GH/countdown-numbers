package io.github.aj8gh.countdown.app;

import io.github.aj8gh.countdown.app.cli.CountdownApp;
import io.github.aj8gh.countdown.conf.AppConfig;
import io.github.aj8gh.countdown.gen.Generator;
import io.github.aj8gh.countdown.out.OutputHandler;
import io.github.aj8gh.countdown.sol.Solver;

public class Main {
    private static final Generator GENERATOR = AppConfig.generator();
    private static final Solver SOLVER = AppConfig.solver();
    private static final OutputHandler OUTPUT_HANDLER = AppConfig.outputHandler();

    private static final CountdownApp APP = new CountdownApp(OUTPUT_HANDLER, GENERATOR, SOLVER);

    public static void main(String... args) {
        APP.run();
    }
}
