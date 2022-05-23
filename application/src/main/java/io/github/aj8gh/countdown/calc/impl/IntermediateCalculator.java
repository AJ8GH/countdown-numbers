package io.github.aj8gh.countdown.calc.impl;

import io.github.aj8gh.countdown.calc.Calculation;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class IntermediateCalculator extends AbstractCalculator {
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
        var results = numbers.stream()
                .map(Calculation::new)
                .collect(toList());

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
