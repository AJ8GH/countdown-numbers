package io.github.aj8gh.countdown.calc;

import static io.github.aj8gh.countdown.calc.Calculator.CalculationMode.INTERMEDIATE;
import static io.github.aj8gh.countdown.calc.Calculator.CalculationMode.SEQUENTIAL;

import io.github.aj8gh.countdown.calc.impl.AbstractCalculator;
import java.util.List;
import java.util.Map;
import lombok.Setter;

public class CalculatorManager extends AbstractCalculator {
    private final Map<CalculationMode, Calculator> calculators;
    private Calculator calculator;
    @Setter
    private boolean switchModes = true;
    @Setter
    private long intermediateThreshold;
    @Setter
    private long sequentialThreshold;

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

    public void adjustMode(long attempts) {
        if (switchModes) {
            if (attempts % intermediateThreshold == 0) {
                setMode(INTERMEDIATE);
            } else if (attempts % sequentialThreshold == 0) {
                setMode(SEQUENTIAL);
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
