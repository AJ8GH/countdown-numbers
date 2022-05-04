package io.github.aj8gh.countdown.app.test;

import io.github.aj8gh.countdown.app.conf.AppConfig;
import io.github.aj8gh.countdown.gen.FilterFactory;
import io.github.aj8gh.countdown.gen.Generator;
import io.github.aj8gh.countdown.sol.Solver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static io.github.aj8gh.countdown.calc.Calculator.CalculationMode;
import static io.github.aj8gh.countdown.calc.Calculator.CalculationMode.INTERMEDIATE;
import static io.github.aj8gh.countdown.calc.Calculator.CalculationMode.RECURSIVE;
import static io.github.aj8gh.countdown.calc.Calculator.CalculationMode.SEQUENTIAL;

public class Tester {
    private static final Logger LOG = LoggerFactory.getLogger(Tester.class);
    private static final Generator GENERATOR = AppConfig.generator();
    private static final Solver SOLVER = AppConfig.solver();
    private static final List<Result> RESULTS = new ArrayList<>();

    public Tester() {
        GENERATOR.addFilter(FilterFactory.Filter.NOT_FIVE.getPredicate())
                        .addFilter(FilterFactory.Filter.ODD.getPredicate());
        SOLVER.setCaching(false);
    }

    private void test(List<Integer> input) {
        runWithMode(INTERMEDIATE, input);
        runWithMode(SEQUENTIAL, input);
        runWithMode(RECURSIVE, input);
        logResults();
        System.out.println("*********************************************");
    }

    private void runWithMode(CalculationMode mode,
                                    List<Integer> input) {
        SOLVER.warmUp();
        SOLVER.setMode(mode);
        SOLVER.solve(input);
        saveResult();
        SOLVER.reset();
    }

    private void saveResult() {
        RESULTS.add(new Result(
                SOLVER.getMode(),
                SOLVER.getTime(),
                SOLVER.getAttempts(),
                SOLVER.getSolution().getSolution(),
                SOLVER.getSolution().getValue(),
                SOLVER.getSolution().getRpn())
        );
    }

    private void logResults() {
        RESULTS.sort(Comparator.comparing(Result::time));
        RESULTS.forEach(r -> LOG.info("{} : {}ms : {} attempts : {} = {} : {}",
                r.mode(), r.time(), r.attempts(), r.solution(), r.result(), r.rpn()));
        RESULTS.clear();
    }
}
