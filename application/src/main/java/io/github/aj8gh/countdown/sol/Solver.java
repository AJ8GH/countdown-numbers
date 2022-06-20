package io.github.aj8gh.countdown.sol;

import io.github.aj8gh.countdown.calc.Calculation;
import io.github.aj8gh.countdown.calc.CalculatorManager;
import io.github.aj8gh.countdown.gen.Generator;

import java.util.List;

import static io.github.aj8gh.countdown.calc.Calculator.CalculationMode;

public class Solver {
    private final CalculatorManager calculator;
    private final SolutionCache cache;
    private final Generator generator;

    private boolean optimiseNumbers;
    private boolean caching;
    private long attempts = 1;
    private int warmups;
    private Calculation solution;

    public Solver(Generator generator,
                  CalculatorManager calculator,
                  SolutionCache cache) {
        this.generator = generator;
        this.calculator = calculator;
        this.cache = cache;
    }

    public Calculation solve(List<Integer> question) {
        if (isCached(question)) return solution;
        int target = question.remove(question.size() - 1);
        if (containsTarget(question, target)) return new Calculation(target);
        calculator.setOptimiseNumbers(optimiseNumbers);
        solution = calculator.calculateSolution(question, target);
        while (solution.getValue() != target) {
            calculator.adjustMode(++attempts);
            solution = calculator.calculateSolution(question, target);
        }
        cache.put(question, solution);
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

    private boolean containsTarget(List<Integer> question, int target) {
        return target == 100 && question.contains(target);
    }

    public void warmup() {
        var saveMode = getMode();
        var saveOptimiseNumbers = optimiseNumbers;
        setOptimiseNumbers(false);
        for (int i = 0; i < warmups; i++) {
            generator.generate(i % 5);
            solve(generator.getQuestionNumbers());
            generator.reset();
            reset();
        }
        calculator.setOptimiseNumbers(optimiseNumbers);
        setOptimiseNumbers(saveOptimiseNumbers);
        setMode(saveMode);
    }

    public void reset() {
        this.attempts = 1;
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

    public void setWarmups(int warmups) {
        this.warmups = warmups;
    }

    public void setOptimiseNumbers(boolean optimiseNumbers) {
        this.optimiseNumbers = optimiseNumbers;
        calculator.setOptimiseNumbers(optimiseNumbers);
    }

    public void setOptimiseNumbersThreshold(long optimiseNumbersThreshold) {
        calculator.setOptimiseNumbersThreshold(optimiseNumbersThreshold);
    }

    public void setCaching(boolean caching) {
        this.caching = caching;
    }
}
