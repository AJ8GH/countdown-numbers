package io.github.aj8gh.countdown.test;

import io.github.aj8gh.countdown.BaseApp;
import io.github.aj8gh.countdown.gen.GenAdaptor;
import io.github.aj8gh.countdown.in.InputSupplier;
import io.github.aj8gh.countdown.sol.Solver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

import static io.github.aj8gh.countdown.calc.Calculator.CalculationMode;
import static io.github.aj8gh.countdown.calc.Calculator.CalculationMode.*;

public class TestApp implements Consumer<String[]> {
    private static final Logger LOG = LoggerFactory.getLogger(TestApp.class);
    private final List<Result> results = new ArrayList<>();

    private final Solver solver;
    private final GenAdaptor generator;
    private final InputSupplier inputSupplier;


    public TestApp(BaseApp baseApp, InputSupplier inputSupplier) {
        this.inputSupplier = inputSupplier;
        this.solver = baseApp.solver();
        this.generator = baseApp.genAdaptor();
        solver.setCaching(false);
    }

    public void accept(String[] args) {
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
        solver.warmUp();
        solver.setMode(mode);
        solver.solve(input);
        saveResult();
        solver.reset();
    }

    private void saveResult() {
        results.add(new Result(
                solver.getMode(),
                solver.getTime(),
                solver.getAttempts(),
                solver.getSolution().getSolution(),
                solver.getSolution().getValue(),
                solver.getSolution().getRpn())
        );
    }

    private void logResults() {
        results.sort(Comparator.comparing(Result::time));
        results.forEach(r -> LOG.info("{} : {}ms : {} attempts : {} = {} : {}",
                r.mode(), r.time(), r.attempts(), r.solution(), r.result(), r.rpn()));
        results.clear();
    }
}
