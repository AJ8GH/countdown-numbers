package io.github.aj8gh.countdown.util.calculator.impl;

import io.github.aj8gh.countdown.util.calculator.Calculator;
import io.github.aj8gh.countdown.util.calculator.Calculation;

import java.util.List;

import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.SEQUENTIAL;
import static java.util.Collections.shuffle;

public class SequentialCalculator implements Calculator {
    private static final CalculationMode MODE = SEQUENTIAL;

    @Override
    public Calculation calculateTarget(List<Integer> numbers) {
        return calculate(numbers, 0);
    }

    @Override
    public Calculation calculateSolution(List<Integer> numbers, int target) {
        return calculate(numbers, target);
    }

    @Override
    public CalculationMode getMode() {
        return MODE;
    }

    private Calculation calculate(List<Integer> numbers, int target) {
        shuffle(numbers);
        var result = new Calculation(numbers.get(0));
        for (int i = 1; i < numbers.size(); i++) {
            if (result.getValue() == target) return result;
            doCalculation(result, numbers.get(i));
        }
        return result;
    }
}
