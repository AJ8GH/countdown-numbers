package io.github.aj8gh.countdown.sol;

import io.github.aj8gh.countdown.calc.Calculation;
import io.github.aj8gh.countdown.calc.Calculator;

import java.util.List;

public class SolAdaptor {
    private static final long DEFAULT_MAX_NUMBER_THRESHOLD = 20_000;
    private static final int DEFAULT_MAX_NUMBERS = 6;

    private final SolutionCache cache;
    private final Solver solver;

    private Calculation solution;
    private boolean caching;
    private boolean checkDifficulty;
    private int maxNumbers = DEFAULT_MAX_NUMBERS;
    private long maxNumberThreshold = DEFAULT_MAX_NUMBER_THRESHOLD;


    public SolAdaptor(SolutionCache cache, Solver solver) {
        this.cache = cache;
        this.solver = solver;
    }

    public Calculation solve(List<Integer> inputNumbers) {
        if (!isCached(inputNumbers)) {
            this.solution = solver.solve(inputNumbers);
            if (checkDifficulty) { // TODO: fix attempts check for recursive mode
                while (solver.getAttempts() < maxNumberThreshold && solution.getNumbers() > maxNumbers) {
                    this.solution = solver.solve(inputNumbers);
                }
            }
        }
        cache.put(inputNumbers, solution);
        solver.reset();
        return solution;
    }

    private boolean isCached(List<Integer> question) {
        if (caching) {
            var cachedSolution = cache.get(question);
            if (cachedSolution != null) {
                this.solution = cachedSolution;
                return true;
            }
        }
        return false;
    }

    public Calculation getSolution() {
        return solution;
    }

    public void setCheckDifficulty(boolean checkDifficulty) {
        this.checkDifficulty = checkDifficulty;
    }

    public void setMaxNumbers(int maxNumbers) {
        this.maxNumbers = maxNumbers;
    }

    public void setMaxNumberThreshold(long maxNumberThreshold) {
        this.maxNumberThreshold = maxNumberThreshold;
    }

    public void setMode(Calculator.CalculationMode mode) {
        solver.setMode(mode);
    }

    public void setCaching(boolean caching) {
        this.caching = caching;
    }

    public void reset() {
        solver.reset();
    }
}
