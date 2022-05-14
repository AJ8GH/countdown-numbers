package io.github.aj8gh.countdown.test;

import io.github.aj8gh.countdown.BaseApp;
import io.github.aj8gh.countdown.in.InputSupplier;
import io.github.aj8gh.countdown.sol.SolAdaptor;
import io.github.aj8gh.countdown.sol.SolResult;
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
    private final List<SolResult> results = new ArrayList<>();

    private final SolAdaptor solver;
    private final InputSupplier inputSupplier;

    public TestApp(BaseApp baseApp) {
        this.inputSupplier = baseApp.inputSupplier();
        this.solver = baseApp.solAdaptor();
        solver.setCaching(false);
    }

    public void accept(String[] args) {
        test(inputSupplier.getSolverInput());
    }

    private void test(List<List<Integer>> inputs) {
        for (var input : inputs) {
            runWithMode(INTERMEDIATE, input);
            runWithMode(SEQUENTIAL, input);
            runWithMode(RECURSIVE, input);
            logResults();
        }
    }

    private void runWithMode(CalculationMode mode, List<Integer> input) {
        solver.getSolver().setMode(mode);
        solver.solve(input);
        saveResult();
    }

    private void saveResult() {
        results.add(solver.getResult());
    }

    private void logResults() {
        results.sort(Comparator.comparing(SolResult::getTime));
        results.forEach(r -> LOG.info("{} : {}ms : {} attempts : {} = {} : {}\n\n",
                r.getMode(), r.getTime(), r.getAttempts(),
                r.getSolution(), r.getTarget(), r.getRpn()));
        results.clear();
    }
}
