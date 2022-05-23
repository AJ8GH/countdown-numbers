package io.github.aj8gh.countdown.calc;

import io.github.aj8gh.countdown.calc.impl.AbstractCalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.github.aj8gh.countdown.calc.Calculator.CalculationMode.INTERMEDIATE;
import static io.github.aj8gh.countdown.calc.Calculator.CalculationMode.RECURSIVE;
import static io.github.aj8gh.countdown.calc.Calculator.CalculationMode.SEQUENTIAL;

public class CalculatorManager extends AbstractCalculator {
    private final Map<CalculationMode, Calculator> calculators;

    private boolean switchModes = true;
    private long intermediateThreshold;
    private long sequentialThreshold;
    private long recursiveThreshold;
    private long useAllNumbersThreshold;
    private boolean useAllNumbers;

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
        var input = new ArrayList<>(numbers);
        if (!useAllNumbers && !getMode().equals(RECURSIVE)) {
            input.remove(RANDOM.nextInt(input.size()));
        }
        return calculator.calculateSolution(input, target);
    }

    public void setUseAllNumbers(boolean useAllNumbers) {
        this.useAllNumbers = useAllNumbers;
    }

    public void adjust(long attempts) {
        if ((!useAllNumbers && attempts > useAllNumbersThreshold) || getMode().equals(RECURSIVE)) {
            setUseAllNumbers(true);
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

    public void setUseAllNumbersThreshold(long useAllNumbersThreshold) {
        this.useAllNumbersThreshold = useAllNumbersThreshold;
    }
}
