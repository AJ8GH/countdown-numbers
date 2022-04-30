package io.github.aj8gh.countdown.util.calculator;

import io.github.aj8gh.countdown.util.calculator.impl.IntermediateCalculator;
import io.github.aj8gh.countdown.util.calculator.impl.RecursiveCalculator;
import io.github.aj8gh.countdown.util.calculator.impl.RpnCalculator;
import io.github.aj8gh.countdown.util.calculator.impl.SequentialCalculator;
import it.unimi.dsi.util.XoRoShiRo128PlusRandom;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toUnmodifiableMap;

public interface Calculator {
    enum CalculationMode {
        SEQUENTIAL("S", SequentialCalculator.class),
        INTERMEDIATE("I", IntermediateCalculator.class),
        RECURSIVE("R", RecursiveCalculator.class),
        RPN("P", RpnCalculator.class);

        private static final Map<String, CalculationMode> FROM_STRING_MAP = Arrays
                .stream(CalculationMode.values())
                .collect(toMap(CalculationMode::letter, Function.identity()));
        private final String letter;
        private final Class<? extends Calculator> type;

        public static CalculationMode fromString(String letter) {
            return FROM_STRING_MAP.get(letter);
        }

        <T extends Calculator> CalculationMode(String letter, Class<T> type) {
            this.letter = letter;
            this.type = type;
        }

        public String letter() {
            return letter;
        }

        public Class<? extends Calculator> type() {
            return type;
        }
    }

    XoRoShiRo128PlusRandom RANDOM = new XoRoShiRo128PlusRandom();
    Map<Integer, Operator> OPERATORS = Arrays.stream(Operator.values())
            .collect(toUnmodifiableMap(Enum::ordinal, Function.identity()));

    Calculation calculateTarget(List<Integer> numbers);

    Calculation calculateSolution(List<Integer> numbers, int target);

    CalculationMode getMode();

    default Calculation doCalculation(Calculation calculation, Integer number) {
        var value = calculation.getValue();
        var result = calculation.calculate(getOperator(), number);
        while (calculation.getValue() == value) {
            result = calculation.calculate(getOperator(), number);
        }
        return result;
    }

    default Calculation doCalculation(Calculation first, Calculation second) {
        var value = first.getValue();
        var result = first.calculate(getOperator(), second);
        while (first.getValue() == value) {
            result = first.calculate(getOperator(), second);
        }
        return result;
    }

    default Operator getOperator() {
        return OPERATORS.get(RANDOM.nextInt(OPERATORS.size()));
    }
}
