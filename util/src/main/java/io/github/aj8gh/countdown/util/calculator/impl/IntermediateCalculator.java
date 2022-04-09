package io.github.aj8gh.countdown.util.calculator.impl;

import io.github.aj8gh.countdown.util.calculator.Calculator;
import io.github.aj8gh.countdown.util.calculator.calculation.Calculation;
import io.github.aj8gh.countdown.util.calculator.calculation.CalculationV2;

import java.util.ArrayList;
import java.util.List;

import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.INTERMEDIATE;

public class IntermediateCalculator implements Calculator {
    private static final CalculationMode MODE = INTERMEDIATE;

    @Override
    public Calculation calculate(List<Integer> numbers) {
        numbers = new ArrayList<>(numbers);
        int target = numbers.remove(numbers.size() - 1);
        return calculateTarget(numbers, target);
    }

    @Override
    public CalculationMode getMode() {
        return MODE;
    }

    private Calculation calculateTarget(List<Integer> numbers, int target) {
        var results = new ArrayList<>(numbers.stream().map(CalculationV2::new).toList());
        while (results.size() > 1) {
            var first = results.get(RANDOM.nextInt(results.size()));
            if (first.getValue() == target) return first;
            var second = results.remove(RANDOM.nextInt(results.size()));
            if (second.getValue() == target) return second;
            calculate(first, second);
        }
        return results.get(0);
    }
}
