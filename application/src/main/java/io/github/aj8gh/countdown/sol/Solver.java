package io.github.aj8gh.countdown.sol;

import io.github.aj8gh.countdown.calc.Calculation;
import io.github.aj8gh.countdown.calc.CalculatorManager;
import io.github.aj8gh.countdown.gen.Generator;

import java.util.List;

import static io.github.aj8gh.countdown.calc.Calculator.CalculationMode;

public class Solver {
    private static final int DEFAULT_WARM_UPS = 20;

    private final CalculatorManager calculator;
    private final Generator generator;
    private int warmups = DEFAULT_WARM_UPS;
    private long attempts = 1;
    private Calculation solution;
    private boolean useAllNumbers = true;

    public Solver(Generator generator, CalculatorManager calculator) {
        this.generator = generator;
        this.calculator = calculator;
    }

    public Calculation solve(List<Integer> question) {
        int target = question.remove(question.size() - 1);
        if (containsTarget(question, target)) return new Calculation(target);
        calculator.setUseAllNumbers(useAllNumbers);
        solution = calculator.calculateSolution(question, target);
        while (solution.getValue() != target) {
            calculator.adjust(++attempts);
            solution = calculator.calculateSolution(question, target);
        }
        return solution;
    }

    private boolean containsTarget(List<Integer> question, int target) {
        return target == 100 && question.contains(target);
    }

    public void warmUp() {
        var mode = getMode();
        calculator.setUseAllNumbers(true);
        for (int i = 0; i < warmups; i++) {
            generator.generate(i % 5);
            solve(generator.getQuestionNumbers());
            generator.reset();
            reset();
        }
        calculator.setUseAllNumbers(useAllNumbers);
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

    public void setUseAllNumbers(boolean useAllNumbers) {
        this.useAllNumbers = useAllNumbers;
        calculator.setUseAllNumbers(useAllNumbers);
    }

    public void setUseAllNumbersThreshold(long useAllNumbersThreshold) {
        calculator.setUseAllNumbersThreshold(useAllNumbersThreshold);
    }
}
