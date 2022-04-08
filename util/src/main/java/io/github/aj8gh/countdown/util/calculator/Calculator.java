package io.github.aj8gh.countdown.util.calculator;

import java.util.List;

public interface Calculator {
    enum CalculationMode {
        SEQUENTIAL, INTERMEDIATE, MIXED, RECURSIVE
    }

    Calculation calculate(List<Integer> numbers);
    CalculationMode getMode();
}
