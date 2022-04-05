package io.github.aj8gh.countdown.solver;

import io.github.aj8gh.countdown.util.calculator.Calculation;
import io.github.aj8gh.countdown.util.calculator.Calculator;
import io.github.aj8gh.countdown.util.timer.Timer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode;
import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.INTERMEDIATE;
import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.RUNNING;

public class SimpleSolver implements Solver {
    private static final CalculationMode DEFAULT_MODE = RUNNING;
    private static final int DEFAULT_MODE_SWITCH_THRESHOLD = 500;

    private final Calculator calculator;
    private final SolutionCache cache;
    private final Timer timer;
    private final AtomicInteger attempts = new AtomicInteger(1);

    private Calculation solution;
    private CalculationMode mode = DEFAULT_MODE;
    private int modeSwitchThreshold = DEFAULT_MODE_SWITCH_THRESHOLD;

    public SimpleSolver(Calculator calculator, SolutionCache cache, Timer timer) {
        this.calculator = calculator;
        this.cache = cache;
        this.timer = timer;
    }

    @Override
    public void solve(List<Integer> question) {
        timer.start();
        var cachedSolution = cache.get(question);
        if (cachedSolution != null) {
            this.solution = cachedSolution;
            timer.stop();
            return;
        }
        int target = question.get(question.size() - 1);
        Calculation calculation = calculator.calculateSolution(new ArrayList<>(question), mode);
        while (calculation.getResult() != target) {
            calculation = calculator.calculateSolution(new ArrayList<>(question), mode);
            if (attempts.incrementAndGet() % modeSwitchThreshold == 0) {
                switchMode();
            }
        }
        this.solution = calculation;
        timer.stop();
        cache.put(question, solution);
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
        return mode;
    }

    @Override
    public void setMode(Calculator.CalculationMode mode) {
        this.mode = mode;
    }

    @Override
    public Calculation getSolution() {
        return solution;
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
        this.mode = mode.equals(RUNNING) ? INTERMEDIATE : RUNNING;
    }
}
