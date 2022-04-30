package io.github.aj8gh.countdown.util.calculator.impl;

import io.github.aj8gh.countdown.util.calculator.Calculation;
import io.github.aj8gh.countdown.util.calculator.Calculator;

import java.util.List;

import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.RPN;

public class RpnCalculator implements Calculator {
    private static final CalculationMode MODE = RPN;

    @Override
    public Calculation calculateTarget(List<Integer> numbers) {
        return null;
    }

    @Override
    public Calculation calculateSolution(List<Integer> numbers, int target) {
        return null;
    }

    @Override
    public CalculationMode getMode() {
        return MODE;
    }
}
