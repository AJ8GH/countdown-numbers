package io.github.aj8gh.countdown.calc;

import io.github.aj8gh.countdown.calc.impl.AbstractCalculator;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.github.aj8gh.countdown.calc.Calculator.CalculationMode.INTERMEDIATE;
import static io.github.aj8gh.countdown.calc.Calculator.CalculationMode.RECURSIVE;
import static io.github.aj8gh.countdown.calc.Calculator.CalculationMode.SEQUENTIAL;

public class CalculatorManager extends AbstractCalculator {
    private final Map<CalculationMode, Calculator> calculators;
    private Calculator calculator;

    @Setter private boolean switchModes = true;
    @Setter private long intermediateThreshold;
    @Setter private long sequentialThreshold;
    @Setter private long recursiveThreshold;
    @Setter private long optimiseNumbersThreshold;
    @Setter private boolean optimiseNumbers;

    public CalculatorManager(Map<CalculationMode, Calculator> calculators) {
        this.calculators = calculators;
    }

    @Override
    public Calculation calculateTarget(List<Integer> numbers) {
        return calculator.calculateTarget(numbers);
    }

    @Override
    public Calculation calculateSolution(List<Integer> numbers, int target) {
        var input = new ArrayList<>(numbers);
        if (optimiseNumbers && !getMode().equals(RECURSIVE)) {
            input.remove(RANDOM.nextInt(input.size()));
        }
        return calculator.calculateSolution(input, target);
    }

    public void adjustMode(long attempts) {
        if (attempts > optimiseNumbersThreshold) {
            this.optimiseNumbers = false;
        }
        if (switchModes) {
            if (attempts % intermediateThreshold == 0) {
                setMode(INTERMEDIATE);
            } else if (attempts % sequentialThreshold == 0) {
                setMode(SEQUENTIAL);
            } else if (attempts % recursiveThreshold == 0) {
                setMode(RECURSIVE);
            }
        }
    }

    public void setMode(CalculationMode mode) {
        this.calculator = calculators.get(mode);
    }

    public CalculationMode getMode() {
        return calculator.getMode();
    }
}
