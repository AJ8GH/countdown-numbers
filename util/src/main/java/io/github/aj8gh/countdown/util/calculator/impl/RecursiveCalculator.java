package io.github.aj8gh.countdown.util.calculator.impl;

import io.github.aj8gh.countdown.util.calculator.Calculation;
import io.github.aj8gh.countdown.util.calculator.Calculator;

import java.util.List;

import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.RECURSIVE;

public class RecursiveCalculator implements Calculator {
    private final CalculationMode mode = RECURSIVE;

    @Override
    public Calculation calculate(List<Integer> numbers) {
        return null;
    }

    @Override
    public CalculationMode getMode() {
        return mode;
    }
}
