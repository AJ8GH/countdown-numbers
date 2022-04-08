package io.github.aj8gh.countdown.util.calculator;

import io.github.aj8gh.countdown.util.calculator.calculation.Calculation;

import java.util.List;

public interface Calculator {
    enum CalculationMode {
        SEQUENTIAL, INTERMEDIATE, MIXED, RECURSIVE
    }

    Calculation calculate(List<Integer> numbers);
    CalculationMode getMode();
}
