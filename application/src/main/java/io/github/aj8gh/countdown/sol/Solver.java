package io.github.aj8gh.countdown.sol;

import io.github.aj8gh.countdown.calc.CalculatorManager;
import io.github.aj8gh.countdown.gen.Generator;
import io.github.aj8gh.countdown.calc.Calculation;
import io.github.aj8gh.countdown.util.Timer;

import java.util.ArrayList;
import java.util.List;

import static io.github.aj8gh.countdown.calc.Calculator.CalculationMode;

public class Solver {
    private static final long DEFAULT_MODE_SWITCH_THRESHOLD = 20_000;
    private static final long DEFAULT_MAX_NUMBER_THRESHOLD = 20_000;
    private static final int DEFAULT_MAX_NUMBERS = 6;
    private static final int DEFAULT_WARM_UPS = 20;

    private final SolutionCache cache = new SolutionCache();
    private final CalculatorManager calculator;
    private final Generator generator;
    private final Timer timer = new Timer();

    private long maxNumberThreshold = DEFAULT_MAX_NUMBER_THRESHOLD;
    private long modeSwitchThreshold = DEFAULT_MODE_SWITCH_THRESHOLD;
    private int maxNumbers = DEFAULT_MAX_NUMBERS;
    private int warmUps = DEFAULT_WARM_UPS;
    private boolean caching = true;
    private long attempts = 1;
    private Calculation solution;

    public Solver(Generator generator, CalculatorManager calculator) {
        this.generator = generator;
        this.calculator = calculator;
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
        while (isUnsolved(calculation, target)) {
            calculation = calculator.calculateSolution(question, target);
            calculator.switchMode(++attempts);
        }
        return calculation;
    }

    private boolean isUnsolved(Calculation calculation, int target) {
        if (attempts < maxNumberThreshold && calculation.getNumbers() > maxNumbers) {
            return true;
        }
        return calculation.getValue() != target;
    }

    private boolean containsTarget(List<Integer> question, int target) {
        return target == 100 && question.contains(target);
    }

    public void warmUp() {
        var mode = getMode();
        for (int i = 0; i < warmUps; i++) {
            generator.generate(i % 5);
            solve(generator.getQuestionNumbers());
            generator.reset();
            reset();
        }
        setMode(mode);
    }

    public void reset() {
        this.attempts = 1;
        timer.reset();
    }

    public double getTime() {
        return timer.getLastTime();
    }

    public long getAttempts() {
        return attempts;
    }

    public CalculationMode getMode() {
        return calculator.getMode();
    }

    public void setMode(CalculationMode mode) {
        calculator.setMode(mode);
    }

    public Calculation getSolution() {
        return solution;
    }

    public void setSwitchModes(boolean switchModes) {
        calculator.setSwitchModes(switchModes);
    }

    public long getModeSwitchThreshold() {
        return modeSwitchThreshold;
    }

    public void setModeSwitchThreshold(long modeSwitchThreshold) {
        this.modeSwitchThreshold = modeSwitchThreshold;
    }

    public void setMaxNumbers(int maxNumbers) {
        this.maxNumbers = maxNumbers;
    }

    public void setMaxNumberThreshold(int maxNumberThreshold) {
        this.maxNumberThreshold = maxNumberThreshold;
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
}
