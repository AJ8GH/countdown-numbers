package io.github.aj8gh.countdown.util.calculator.impl;

import io.github.aj8gh.countdown.util.calculator.Calculator;
import io.github.aj8gh.countdown.util.calculator.Operator;
import io.github.aj8gh.countdown.util.calculator.calculation.Calculation;
import io.github.aj8gh.countdown.util.calculator.calculation.Calculation2;
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
    private static final CalculationMode MODE = SEQUENTIAL;
    private static final XoRoShiRo128PlusRandom RANDOM = new XoRoShiRo128PlusRandom();
    private static final Map<Integer, Operator> OPERATORS = Arrays
            .stream(Operator.values())
            .collect(toMap(Enum::ordinal, Function.identity()));

    @Override
    public Calculation calculate(List<Integer> numbers) {
        numbers = new ArrayList<>(numbers);
        int target = numbers.remove(numbers.size() - 1);
        return calculateTarget(numbers, target) ;
    }

    @Override
    public CalculationMode getMode() {
        return mode;
    }

    private Calculation calculateTarget(List<Integer> numbers, int target) {
        shuffle(numbers);
        var result = new Calculation2(numbers.get(0));
        for (int i = 1; i < numbers.size(); i++) {
            if (result.getValue() == target) return result;
            calculate(result, numbers.get(i));
        }
        return result;
    }

    private void calculate(Calculation result, Integer number) {
        result.calculate(getOperator(), number);
        while (result.getValue() == 0) {
            result.calculate(getOperator(), number);
        }
    }

    private Operator getOperator() {
        return OPERATORS.get(RANDOM.nextInt(OPERATORS.size()));
    }
}
