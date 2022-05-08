package io.github.aj8gh.countdown.sol;

import io.github.aj8gh.countdown.calc.Calculation;
import io.github.aj8gh.countdown.calc.Calculator;
import io.github.aj8gh.countdown.util.Timer;

import java.util.List;

public class SolAdaptor {
    private static final long DEFAULT_MAX_NUMBER_THRESHOLD = 20_000;
    private static final int DEFAULT_MAX_NUMBERS = 6;

    private final SolutionCache cache;
    private final Solver solver;
    private final Timer timer;

    private SolResult solResult;
    private Calculation solution;
    private boolean caching;
    private boolean checkDifficulty;
    private int maxNumbers = DEFAULT_MAX_NUMBERS;
    private long maxNumberThreshold = DEFAULT_MAX_NUMBER_THRESHOLD;


    public SolAdaptor(SolutionCache cache, Timer timer, Solver solver) {
        this.cache = cache;
        this.timer = timer;
        this.solver = solver;
    }

    public SolResult solve(List<Integer> inputNumbers) {
        solver.warmUp();
        timer.start();
        runSolver(inputNumbers);
        timer.stop();
        return recordResult();
    }

    private void runSolver(List<Integer> inputNumbers) {
        if (isCached(inputNumbers)) return;
        this.solution = solver.solve(inputNumbers);
        if (checkDifficulty) { // TODO: fix attempts check for recursive mode
            while (solver.getAttempts() < maxNumberThreshold && solution.getNumbers() > maxNumbers) {
                this.solution = solver.solve(inputNumbers);
            }
        }
    }

    private boolean isCached(List<Integer> question) {
        if (!caching) return false;
        var cachedSolution = cache.get(question);
        if (cachedSolution != null) {
            this.solution = cachedSolution;
            return true;
        }
        return false;
    }

    private SolResult recordResult() {
        this.solResult = SolResult.builder()
                .solution(solution.getSolution())
                .rpn(solution.getRpn())
                .target(solution.getValue())
                .attempts(solver.getAttempts())
                .time(timer.getTime())
                .mode(solver.getMode())
                .build();
        solver.reset();
        return solResult;
    }

    public SolResult getResult() {
        return solResult;
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

    public Solver getSolver() {
        return solver;
    }
}
