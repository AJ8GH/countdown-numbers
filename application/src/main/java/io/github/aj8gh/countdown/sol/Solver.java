package io.github.aj8gh.countdown.sol;

import io.github.aj8gh.countdown.calc.CalculatorManager;
import io.github.aj8gh.countdown.gen.Generator;
import io.github.aj8gh.countdown.calc.Calculation;

import java.util.ArrayList;
import java.util.List;

import static io.github.aj8gh.countdown.calc.Calculator.CalculationMode;

public class Solver {
    private static final int DEFAULT_WARM_UPS = 20;

    private final CalculatorManager calculator;
    private final Generator generator;

    private int warmups = DEFAULT_WARM_UPS;
    private long attempts = 1;
    private Calculation solution;

    public Solver(Generator generator, CalculatorManager calculator) {
        this.generator = generator;
        this.calculator = calculator;
    }

    public Calculation solve(List<Integer> question) {
        this.solution = calculate(new ArrayList<>(question));
        return solution;
    }

    private Calculation calculate(List<Integer> question) {
        int target = question.remove(question.size() - 1);
        if (containsTarget(question, target)) return new Calculation(target);
        Calculation calculation = calculator.calculateSolution(question, target);
        while (calculation.getValue() != target) {
            calculation = calculator.calculateSolution(question, target);
            calculator.switchMode(++attempts);
        }
        return calculation;
    }

    private boolean containsTarget(List<Integer> question, int target) {
        return target == 100 && question.contains(target);
    }

    public void warmUp() {
        var mode = getMode();
        for (int i = 0; i < warmups; i++) {
            generator.generate(i % 5);
            solve(generator.getQuestionNumbers());
            generator.reset();
            reset();
        }
        setMode(mode);
    }

    public void reset() {
        this.attempts = 1;
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

    public void setWarmups(int warmups) {
        this.warmups = warmups;
    }
}
