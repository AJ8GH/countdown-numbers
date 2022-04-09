package io.github.aj8gh.countdown.util.calculator;

import io.github.aj8gh.countdown.util.calculator.calculation.Calculation;
import it.unimi.dsi.util.XoRoShiRo128PlusRandom;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

public interface Calculator {
    enum CalculationMode {
        SEQUENTIAL, INTERMEDIATE, MIXED, RECURSIVE
    }

    XoRoShiRo128PlusRandom RANDOM = new XoRoShiRo128PlusRandom();
    Map<Integer, Operator> OPERATORS = Arrays.stream(Operator.values())
            .collect(toMap(Enum::ordinal, Function.identity()));

    Calculation calculate(List<Integer> numbers);
    CalculationMode getMode();

    default void calculate(Calculation calculation, Integer number) {
        calculation.calculate(getOperator(), number);
        while (calculation.getValue() == 0) {
            calculation.calculate(getOperator(), number);
        }
    }

    default void calculate(Calculation first, Calculation second) {
        first.calculate(getOperator(), second);
        while (first.getValue() == 0) {
            first.calculate(getOperator(), second);
        }
    }

    default Operator getOperator() {
        return OPERATORS.get(RANDOM.nextInt(OPERATORS.size()));
    }
}
