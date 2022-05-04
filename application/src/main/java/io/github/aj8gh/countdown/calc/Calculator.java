package io.github.aj8gh.countdown.calc;

import io.github.aj8gh.countdown.calc.impl.IntermediateCalculator;
import io.github.aj8gh.countdown.calc.impl.RecursiveCalculator;
import io.github.aj8gh.countdown.calc.impl.RpnCalculator;
import io.github.aj8gh.countdown.calc.impl.SequentialCalculator;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

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

    Calculation calculateTarget(List<Integer> numbers);

    Calculation calculateSolution(List<Integer> numbers, int target);

    CalculationMode getMode();
}
