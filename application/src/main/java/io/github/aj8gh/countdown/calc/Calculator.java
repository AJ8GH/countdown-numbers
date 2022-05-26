package io.github.aj8gh.countdown.calc;

import java.util.List;

public interface Calculator {
    enum CalculationMode {
        INTERMEDIATE, SEQUENTIAL, RECURSIVE
    }

    Calculation calculateTarget(List<Integer> numbers);
    Calculation calculateSolution(List<Integer> numbers, int target);
    CalculationMode getMode();
}
