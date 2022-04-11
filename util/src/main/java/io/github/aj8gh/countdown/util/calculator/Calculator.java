package io.github.aj8gh.countdown.util.calculator;

import it.unimi.dsi.util.XoRoShiRo128PlusRandom;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toUnmodifiableMap;

public interface Calculator {
    enum CalculationMode {
        SEQUENTIAL,
        INTERMEDIATE,
        RECURSIVE,
        MIXED
    }

    XoRoShiRo128PlusRandom RANDOM = new XoRoShiRo128PlusRandom();
    Map<Integer, Operator> OPERATORS = Arrays.stream(Operator.values())
            .collect(toUnmodifiableMap(Enum::ordinal, Function.identity()));

    Calculation calculateTarget(List<Integer> numbers);

    Calculation calculateSolution(List<Integer> numbers, int target);

    CalculationMode getMode();

    default Calculation doCalculation(Calculation calculation, Integer number) {
        var result = calculation.calculate(getOperator(), number);
        while (calculation.getValue() == 0) {
            result = calculation.calculate(getOperator(), number);
        }
        return result;
    }

    default Calculation doCalculation(Calculation first, Calculation second) {
        var result = first.calculate(getOperator(), second);
        while (first.getValue() == 0) {
            result = first.calculate(getOperator(), second);
        }
        return result;
    }

    default Operator getOperator() {
        return OPERATORS.get(RANDOM.nextInt(OPERATORS.size()));
    }
}
