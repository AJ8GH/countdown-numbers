package io.github.aj8gh.countdown.util.calculator.impl;

import io.github.aj8gh.countdown.util.calculator.calculation.CalculationImpl;
import io.github.aj8gh.countdown.util.calculator.Calculator;

import java.util.List;

import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.INTERMEDIATE;

public class IntermediateCalculator implements Calculator {
    private static final CalculationMode MODE = INTERMEDIATE;

    @Override
    public CalculationImpl calculate(List<Integer> numbers) {
        return null;
    }

    @Override
    public CalculationMode getMode() {
        return MODE;
    }
}
