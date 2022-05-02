package io.github.aj8gh.countdown.calc.impl;

import io.github.aj8gh.countdown.calc.Calculation;
import io.github.aj8gh.countdown.calc.Calculator;

import java.util.ArrayList;
import java.util.List;

public class IntermediateCalculator implements Calculator {
    private static final CalculationMode MODE = CalculationMode.INTERMEDIATE;

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
        var results = new ArrayList<>(numbers.stream()
                .map(Calculation::new)
                .toList());

        while (results.size() > 1) {
            var first = results.remove(RANDOM.nextInt(results.size()));
            var second = results.remove(RANDOM.nextInt(results.size()));
            var result = doCalculation(first, second);
            if (result.getValue() == target) return result;
            results.add(result);
        }
        return results.get(0);
    }
}
