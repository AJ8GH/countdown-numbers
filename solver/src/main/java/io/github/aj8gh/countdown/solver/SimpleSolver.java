package io.github.aj8gh.countdown.solver;

import io.github.aj8gh.countdown.util.calculator.Calculation;
import io.github.aj8gh.countdown.util.calculator.Calculator;
import io.github.aj8gh.countdown.util.timer.Timer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.INTERMEDIATE;
import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.SEQUENTIAL;

public class SimpleSolver implements Solver {
    private static final int DEFAULT_MODE_SWITCH_THRESHOLD = 500;

    private final Calculator calculator;
    private final SolutionCache cache;
    private final Timer timer;
    private final AtomicInteger attempts = new AtomicInteger(1);

    private Calculation solution;
    private int modeSwitchThreshold = DEFAULT_MODE_SWITCH_THRESHOLD;

    public SimpleSolver(Calculator calculator, SolutionCache cache, Timer timer) {
        this.calculator = calculator;
        this.cache = cache;
        this.timer = timer;
    }

    @Override
    public void solve(List<Integer> question) {
        timer.start();
        if (isCached(question)) return;
        this.solution = calculateTarget(question);
        cache.put(question, solution);
        timer.stop();
    }

    @Override
    public void reset() {
        this.attempts.set(1);
        timer.reset();
    }

    @Override
    public double getTime() {
        return timer.getLastTime();
    }

    @Override
    public int getAttempts() {
        return attempts.get();
    }

    @Override
    public Calculator.CalculationMode getMode() {
        return calculator.getMode();
    }

    @Override
    public void setMode(Calculator.CalculationMode mode) {
        calculator.setMode(mode);
    }

    @Override
    public Calculation getSolution() {
        return solution;
    }

    @Override
    public int getModeSwitchThreshold() {
        return modeSwitchThreshold;
    }

    @Override
    public void setModeSwitchThreshold(int modeSwitchThreshold) {
        this.modeSwitchThreshold = modeSwitchThreshold;
    }

    @Override
    public double getTotalTime() {
        return timer.getTotalTime();
    }

    @Override
    public void setTimeScale(int timeScale) {
        timer.setTimescale(timeScale);
    }

    private void switchMode() {
        switch (calculator.getMode()) {
            case INTERMEDIATE -> calculator.setMode(SEQUENTIAL); // temporary hack while MIXED mode is broken
            case SEQUENTIAL -> calculator.setMode(INTERMEDIATE);
            case MIXED -> calculator.setMode(SEQUENTIAL);
        }
    }

    private Calculation calculateTarget(List<Integer> question) {
        int target = question.get(question.size() - 1);
        Calculation calculation = calculator.calculateSolution(new ArrayList<>(question));
        while (calculation.getValue() != target) {
            calculation = calculator.calculateSolution(new ArrayList<>(question));
            if (isSwitchThresholdBreached()) switchMode();
        }
        return calculation;
    }

    private boolean isCached(List<Integer> question) {
        var cachedSolution = cache.get(question);
        if (cachedSolution != null) {
            this.solution = cachedSolution;
            timer.stop();
            return true;
        }
        return false;
    }

    private boolean isSwitchThresholdBreached() {
        return attempts.incrementAndGet() % modeSwitchThreshold == 0;
    }
}
