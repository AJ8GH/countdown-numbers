package io.github.aj8gh.countdown.calc;

import java.util.List;
import java.util.Map;

import static io.github.aj8gh.countdown.calc.Calculator.CalculationMode.*;

public class CalculatorManager implements Calculator {
    private final Map<CalculationMode, Calculator> calculators;

    private boolean switchModes = true;
    private long intermediateThreshold;
    private long sequentialThreshold;
    private long recursiveThreshold;

    private Calculator calculator;

    public CalculatorManager(Map<CalculationMode, Calculator> calculators) {
        this.calculators = calculators;
    }

    @Override
    public Calculation calculateTarget(List<Integer> numbers) {
        return calculator.calculateTarget(numbers);
    }

    @Override
    public Calculation calculateSolution(List<Integer> numbers, int target) {
        return calculator.calculateSolution(numbers, target);
    }

    public void setMode(CalculationMode mode) {
        this.calculator = calculators.get(mode);
    }

    public CalculationMode getMode() {
        return calculator.getMode();
    }

    public void switchMode(long attempts) {
        if (!switchModes) return;
        if (attempts % intermediateThreshold == 0) {
            setMode(INTERMEDIATE);
        } else if (attempts % sequentialThreshold == 0) {
            setMode(SEQUENTIAL);
        } else if (attempts % recursiveThreshold == 0) {
            setMode(RECURSIVE);
        }
    }

    public void setSwitchModes(boolean switchModes) {
        this.switchModes = switchModes;
    }

    public void setSequentialThreshold(long sequentialThreshold) {
        this.sequentialThreshold = sequentialThreshold;
    }

    public void setIntermediateThreshold(long intermediateThreshold) {
        this.intermediateThreshold = intermediateThreshold;
    }

    public void setRecursiveThreshold(long recursiveThreshold) {
        this.recursiveThreshold = recursiveThreshold;
    }
}
