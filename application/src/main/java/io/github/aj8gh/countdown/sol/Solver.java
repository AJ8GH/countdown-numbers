package io.github.aj8gh.countdown.sol;

import io.github.aj8gh.countdown.gen.Generator;
import io.github.aj8gh.countdown.calc.Calculation;
import io.github.aj8gh.countdown.calc.Calculator;
import io.github.aj8gh.countdown.util.Timer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static io.github.aj8gh.countdown.calc.Calculator.CalculationMode;
import static io.github.aj8gh.countdown.calc.Calculator.CalculationMode.INTERMEDIATE;
import static io.github.aj8gh.countdown.calc.Calculator.CalculationMode.RECURSIVE;
import static io.github.aj8gh.countdown.calc.Calculator.CalculationMode.SEQUENTIAL;

public class Solver {
    private static final CalculationMode DEFAULT_MODE = INTERMEDIATE;
    private static final long DEFAULT_MODE_SWITCH_THRESHOLD = 20_000;
    private static final int DEFAULT_WARM_UPS = 20;
    private static final boolean DEFAULT_SWITCH_MODES = true;

    private final AtomicInteger attempts = new AtomicInteger(1);
    private final SolutionCache cache = new SolutionCache();
    private final Map<CalculationMode, Calculator> calculators;
    private final Generator generator;
    private final Timer timer = new Timer();

    private boolean switchModes = DEFAULT_SWITCH_MODES;
    private long modeSwitchThreshold = DEFAULT_MODE_SWITCH_THRESHOLD;
    private int warmUps = DEFAULT_WARM_UPS;
    private boolean caching = true;
    private Calculator calculator;
    private Calculation solution;

    public Solver(Generator generator, Map<CalculationMode, Calculator> calculators) {
        this.generator = generator;
        this.calculators = calculators;
        this.calculator = calculators.get(DEFAULT_MODE);
    }

    public void solve(List<Integer> question) {
        timer.start();
        if (isCached(question)) return;
        this.solution = calculate(new ArrayList<>(question));
        cache.put(question, solution);
        timer.stop();
    }

    private boolean isCached(List<Integer> question) {
        if (!caching) return false;
        var cachedSolution = cache.get(question);
        if (cachedSolution != null) {
            this.solution = cachedSolution;
            timer.stop();
            return true;
        }
        return false;
    }

    private Calculation calculate(List<Integer> question) {
        int target = question.remove(question.size() - 1);
        if (containsTarget(question, target)) return new Calculation(target);
        Calculation calculation = calculator.calculateSolution(question, target);
        while (calculation.getValue() != target) {
            calculation = calculator.calculateSolution(question, target);
            if (isSwitchThresholdBreached(attempts.incrementAndGet())) switchMode();
        }
        return calculation;
    }

    private boolean containsTarget(List<Integer> question, int target) {
        return target == 100 && question.contains(target);
    }

    private boolean isSwitchThresholdBreached(int attempts) {
        return switchModes && attempts % modeSwitchThreshold == 0;
    }

    public void warmUp() {
        for (int i = 0; i < warmUps; i++) {
            generator.generate(i % 5);
            solve(generator.getQuestionNumbers());
            generator.reset();
            reset();
        }
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

    public void setSwitchModes(boolean switchModes) {
        this.switchModes = switchModes;
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

    public void setWarmUps(int warmUps) {
        this.warmUps = warmUps;
    }

    public void setCaching(boolean caching) {
        this.caching = caching;
    }

    private void switchMode() {
        this.calculator = calculator.getMode().equals(INTERMEDIATE) ?
                calculators.get(SEQUENTIAL) : calculators.get(RECURSIVE);
    }
}
