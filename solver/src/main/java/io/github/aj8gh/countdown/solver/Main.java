package io.github.aj8gh.countdown.solver;

import io.github.aj8gh.countdown.calculator.Calculator;
import io.github.aj8gh.countdown.calculator.timer.Timer;
import io.github.aj8gh.countdown.generator.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private static final int WARM_UPS = 20;
    private static final Calculator CALCULATOR = new Calculator();
    private static final Generator GENERATOR = new Generator(CALCULATOR, new Timer(), WARM_UPS);
    private static final Solver SOLVER = new SimpleSolver(CALCULATOR, new Timer());

    static {
        warmUp();
    }

    public static void main(String[] args) {
        try {
            var numbers = Arrays.stream(args).map(Integer::parseInt).toList();
            SOLVER.solve(numbers);
            LOG.info("{}", SOLVER);
        } catch (Exception e) {
            LOG.error("Error processing {}", Arrays.asList(args));
        }
    }

    private static void warmUp() {
        for (int i = 0; i < WARM_UPS; i++) {
            SOLVER.solve(GENERATOR.generateQuestionNumbers(ThreadLocalRandom.current().nextInt(5)));
            SOLVER.reset();
            GENERATOR.reset();
        }
    }
}
