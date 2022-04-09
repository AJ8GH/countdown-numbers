package io.github.aj8gh.countdown.util.calculator.impl;

import io.github.aj8gh.countdown.util.calculator.Calculator;
import io.github.aj8gh.countdown.util.calculator.calculation.Calculation;
import io.github.aj8gh.countdown.util.calculator.calculation.CalculationV2;

import java.util.ArrayList;
import java.util.List;

import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.SEQUENTIAL;
import static java.util.Collections.shuffle;

public class SequentialCalculator implements Calculator {
    private static final CalculationMode MODE = SEQUENTIAL;

    @Override
    public Calculation calculate(List<Integer> numbers) {
        numbers = new ArrayList<>(numbers);
        int target = numbers.remove(numbers.size() - 1);
        return calculateTarget(numbers, target) ;
    }

    @Override
    public CalculationMode getMode() {
        return MODE;
    }

    private Calculation calculateTarget(List<Integer> numbers, int target) {
        shuffle(numbers);
        var result = new CalculationV2(numbers.get(0));
        for (int i = 1; i < numbers.size(); i++) {
            if (result.getValue() == target) return result;
            calculate(result, numbers.get(i));
        }
        return result;
    }
}
