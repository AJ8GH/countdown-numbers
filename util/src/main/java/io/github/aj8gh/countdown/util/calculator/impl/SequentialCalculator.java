package io.github.aj8gh.countdown.util.calculator.impl;

import io.github.aj8gh.countdown.util.calculator.Calculation;
import io.github.aj8gh.countdown.util.calculator.Calculator;
import io.github.aj8gh.countdown.util.calculator.Operator;
import it.unimi.dsi.util.XoRoShiRo128PlusRandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.SEQUENTIAL;
import static io.github.aj8gh.countdown.util.calculator.Operator.DIVIDE;
import static java.util.Collections.shuffle;
import static java.util.stream.Collectors.toMap;

public class SequentialCalculator implements Calculator {
    private static final XoRoShiRo128PlusRandom RANDOM = new XoRoShiRo128PlusRandom();
    private static final Map<Integer, Operator> OPERATORS = Arrays.stream(Operator.values())
            .collect(toMap(Enum::ordinal, Function.identity()));
    private static final CalculationMode mode = SEQUENTIAL;
    private final Map<Integer, Integer> numberMap = new HashMap<>();

    @Override
    public Calculation calculate(List<Integer> numbers) {
        numbers = new ArrayList<>(numbers);
        int target = numbers.remove(numbers.size() - 1);
        return calculateRunning(numbers, target) ;
    }

    @Override
    public CalculationMode getMode() {
        return mode;
    }

    private Calculation calculateRunning(List<Integer> numbers, int target) {
        shuffle(numbers);
        var x = new Calculation(numbers.get(0));
        for (int i = 1; i < numbers.size(); i++) {
            if (x.getValue() == target) return x;
            x = calculate(x, numbers.get(i));
        }
        return x;
    }

    private Calculation calculate(Calculation x, Integer y) {
        Calculation result = null;
        while (result == null) {
            result = getResult(getOperator(), x, y);
        }
        return result;
    }

    private Calculation getResult(Operator operator, Calculation x, Integer y) {
        if (operator.apply(x.getValue(), y) == 0) return null;
        if (operator.equals(DIVIDE)) {
            if (x.getValue() % y == 0) {
                return Calculation.calculate(x, operator, y);
            } else if (y % x.getValue() == 0) {
                return Calculation.calculate(y, operator, x);
            }
            return null;
        }
        return Calculation.calculate(x, operator, y);
    }

    private Operator getOperator() {
        return OPERATORS.get(RANDOM.nextInt(OPERATORS.size()));
    }
}
