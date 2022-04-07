package io.github.aj8gh.countdown.solver;

import io.github.aj8gh.countdown.util.calculator.Calculation;
import io.github.aj8gh.countdown.util.calculator.Calculator;
import io.github.aj8gh.countdown.util.calculator.impl.CalculatorImpl;
import io.github.aj8gh.countdown.util.calculator.impl.IntermediateCalculator;
import io.github.aj8gh.countdown.util.calculator.impl.RecursiveCalculator;
import io.github.aj8gh.countdown.util.calculator.impl.SequentialCalculator;
import io.github.aj8gh.countdown.util.timer.Timer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.INTERMEDIATE;
import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.MIXED;
import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.RECURSIVE;
import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.SEQUENTIAL;
import static io.github.aj8gh.countdown.util.calculator.impl.CalculatorImpl.CalculationMode;

public class SimpleSolver implements Solver {
    private static final int DEFAULT_MODE_SWITCH_THRESHOLD = 5_000_000;
    private static final CalculationMode DEFAULT_MODE = SEQUENTIAL;

    private final SolutionCache cache = new SolutionCache();
    private final Timer timer = new Timer();
    private final AtomicInteger attempts = new AtomicInteger(1);
    private final Map<CalculationMode, Calculator> calculators = Map.of(
            SEQUENTIAL, new SequentialCalculator(),
            INTERMEDIATE, new IntermediateCalculator(),
            RECURSIVE, new RecursiveCalculator(),
            MIXED, new CalculatorImpl()
    );

    private Calculator calculator = calculators.get(DEFAULT_MODE);
    private Calculation solution;
    private int modeSwitchThreshold = DEFAULT_MODE_SWITCH_THRESHOLD;

    @Override
    public void solve(List<Integer> question) {
        timer.start();
        if (isCached(question)) return;
        this.solution = calculate(question);
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
    public CalculatorImpl.CalculationMode getMode() {
        return calculator.getMode();
    }

    @Override
    public void setMode(CalculationMode mode) {
        this.calculator = calculators.get(mode);
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

    private Calculation calculate(List<Integer> question) {
        int target = question.get(question.size() - 1);
        Calculation calculation = calculator.calculate(question);
        while (calculation.getValue() != target) {
            calculation = calculator.calculate(question);
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

    private void switchMode() {
        switch (calculator.getMode()) {
            case INTERMEDIATE -> this.calculator = calculators.get(SEQUENTIAL);
            case SEQUENTIAL -> this.calculator = calculators.get(INTERMEDIATE);
        }
    }
}
