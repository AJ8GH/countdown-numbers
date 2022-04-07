package io.github.aj8gh.countdown.util.calculator.impl;

import io.github.aj8gh.countdown.util.calculator.Calculation;
import io.github.aj8gh.countdown.util.calculator.Calculator;
import io.github.aj8gh.countdown.util.calculator.Operator;
import it.unimi.dsi.util.XoRoShiRo128PlusRandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.SEQUENTIAL;
import static java.util.Collections.shuffle;
import static java.util.stream.Collectors.toMap;

public class SequentialCalculator implements Calculator {
    private static final XoRoShiRo128PlusRandom RANDOM = new XoRoShiRo128PlusRandom();
    private static final Map<Integer, Operator> OPERATORS = Arrays.stream(Operator.values())
            .collect(toMap(Enum::ordinal, Function.identity()));

    private static final CalculationMode mode = SEQUENTIAL;

    @Override
    public Calculation calculate(List<Integer> numbers) {
        var target = extractTarget(numbers);
        var calculations = toCalculations(numbers);
        shuffle(calculations);
        var x = calculations.get(0);
        for (int i = 1; i < calculations.size(); i++) {
            if (x.getValue() == target) return x;
            x = calculateUntilValid(x, calculations.get(i), getOperator());
        }
        return x;
    }

    @Override
    public CalculationMode getMode() {
        return mode;
    }

    private List<Calculation> toCalculations(List<Integer> numbers) {
        return new ArrayList<>(numbers.stream()
                .map(Calculation::new)
                .toList());
    }

    private int extractTarget(List<Integer> numbers) {
        return numbers.remove(numbers.size() - 1);
    }

    private Operator getOperator() {
        return OPERATORS.get(RANDOM.nextInt(OPERATORS.size()));
    }
}
