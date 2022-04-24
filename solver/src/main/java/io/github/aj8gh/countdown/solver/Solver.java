package io.github.aj8gh.countdown.solver;

import io.github.aj8gh.countdown.generator.Generator;
import io.github.aj8gh.countdown.util.calculator.Calculation;
import io.github.aj8gh.countdown.util.calculator.Calculator;
import io.github.aj8gh.countdown.util.calculator.impl.IntermediateCalculator;
import io.github.aj8gh.countdown.util.calculator.impl.RecursiveCalculator;
import io.github.aj8gh.countdown.util.calculator.impl.SequentialCalculator;
import io.github.aj8gh.countdown.util.timer.Timer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode;
import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.INTERMEDIATE;
import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.RECURSIVE;
import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.SEQUENTIAL;

public class Solver {
    private static final CalculationMode DEFAULT_MODE = INTERMEDIATE;
    private static final long DEFAULT_MODE_SWITCH_THRESHOLD = 20_000;

    private final Timer timer = new Timer();
    private final SolutionCache cache = new SolutionCache();
    private final Generator generator = new Generator();
    private final AtomicInteger attempts = new AtomicInteger(1);
    private final Map<CalculationMode, Calculator> calculators = Map.of(
            SEQUENTIAL, new SequentialCalculator(),
            INTERMEDIATE, new IntermediateCalculator(),
            RECURSIVE, new RecursiveCalculator());

    private Calculator calculator = calculators.get(DEFAULT_MODE);
    private long modeSwitchThreshold = DEFAULT_MODE_SWITCH_THRESHOLD;
    private Calculation solution;

    public void solve(List<Integer> question) {
        timer.start();
        if (isCached(question)) return;
        this.solution = calculate(new ArrayList<>(question));
        cache.put(question, solution);
        timer.stop();
    }

    public void reset() {
        this.attempts.set(1);
        timer.reset();
    }

    public double getTime() {
        return timer.getLastTime();
    }

    public int getAttempts() {
        return attempts.get();
    }

    public CalculationMode getMode() {
        return calculator.getMode();
    }

    public void setMode(CalculationMode mode) {
        this.calculator = calculators.get(mode);
    }

    public Calculation getSolution() {
        return solution;
    }

    public long getModeSwitchThreshold() {
        return modeSwitchThreshold;
    }

    public void setModeSwitchThreshold(long modeSwitchThreshold) {
        this.modeSwitchThreshold = modeSwitchThreshold;
    }

    public double getTotalTime() {
        return timer.getTotalTime();
    }

    public void setTimeScale(int timeScale) {
        timer.setTimescale(timeScale);
    }

    public void warmUp(int warmUps) {
        for (int i = 0; i < warmUps; i++) {
            generator.generate(i % 5);
            solve(generator.getQuestionNumbers());
            generator.reset();
            reset();
        }
    }

    private Calculation calculate(List<Integer> question) {
        int target = question.remove(question.size() - 1);
        if (containsTarget(question, target)) return new Calculation(target);
        Calculation calculation = calculator.calculateSolution(question, target);
        while (calculation.getValue() != target) {
            calculation = calculator.calculateSolution(question, target);
            if (isSwitchThresholdBreached()) switchMode();
        }
        return calculation;
    }

    private boolean containsTarget(List<Integer> question, int target) {
        if (target != 100) return false;
        return question.contains(target);
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
            case SEQUENTIAL, RECURSIVE -> this.calculator = calculators.get(INTERMEDIATE);
            case INTERMEDIATE -> this.calculator = calculators.get(SEQUENTIAL);
        }
    }
}
