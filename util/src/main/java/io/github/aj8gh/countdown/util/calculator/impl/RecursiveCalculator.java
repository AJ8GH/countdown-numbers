package io.github.aj8gh.countdown.util.calculator.impl;

import io.github.aj8gh.countdown.util.calculator.Calculator;
import io.github.aj8gh.countdown.util.calculator.calculation.Calculation;

import java.util.List;

import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.RECURSIVE;

public class RecursiveCalculator implements Calculator {
    private static final CalculationMode MODE = RECURSIVE;

    @Override
    public Calculation calculate(List<Integer> numbers) {
        return null;
    }

    @Override
    public CalculationMode getMode() {
        return MODE;
    }
}
