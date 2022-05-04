package io.github.aj8gh.countdown.test;

import io.github.aj8gh.countdown.conf.AppConfig;
import io.github.aj8gh.countdown.in.InputSupplier;
import io.github.aj8gh.countdown.sol.Solver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

import static io.github.aj8gh.countdown.calc.Calculator.CalculationMode;
import static io.github.aj8gh.countdown.calc.Calculator.CalculationMode.INTERMEDIATE;
import static io.github.aj8gh.countdown.calc.Calculator.CalculationMode.RECURSIVE;
import static io.github.aj8gh.countdown.calc.Calculator.CalculationMode.SEQUENTIAL;

public class TestApp implements Consumer<String[]> {
    private static final Logger LOG = LoggerFactory.getLogger(TestApp.class);
    private static final AppConfig CONFIG = new AppConfig();
    private static final Solver SOLVER = CONFIG.solver();
    private static final List<Result> RESULTS = new ArrayList<>();

    private final InputSupplier inputSupplier;

    public TestApp(InputSupplier inputSupplier) {
        this.inputSupplier = inputSupplier;
        SOLVER.setCaching(false);
    }

    public void accept(String[] args) {
        testMultipleInputs(Inputs.TRICKY);
        test(inputSupplier.getSolverInput());
    }

    private void testMultipleInputs(List<List<Integer>> inputs) {
        inputs.forEach(this::test);
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
