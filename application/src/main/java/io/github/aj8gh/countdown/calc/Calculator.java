package io.github.aj8gh.countdown.calc;

import io.github.aj8gh.countdown.calc.impl.IntermediateCalculator;
import io.github.aj8gh.countdown.calc.impl.RecursiveCalculator;
import io.github.aj8gh.countdown.calc.impl.SequentialCalculator;

import java.util.List;

public interface Calculator {
    enum CalculationMode {
        INTERMEDIATE(IntermediateCalculator.class),
        SEQUENTIAL(SequentialCalculator.class),
        RECURSIVE(RecursiveCalculator.class);

        private final Class<? extends Calculator> type;

        <T extends Calculator> CalculationMode(Class<T> type) {
            this.type = type;
        }

        public Class<? extends Calculator> type() {
            return type;
        }
    }

    Calculation calculateTarget(List<Integer> numbers);
    Calculation calculateSolution(List<Integer> numbers, int target);
    CalculationMode getMode();
}
