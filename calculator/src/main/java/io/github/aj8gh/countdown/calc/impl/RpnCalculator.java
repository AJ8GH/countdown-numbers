package io.github.aj8gh.countdown.calc.impl;

import io.github.aj8gh.countdown.calc.Calculation;
import io.github.aj8gh.countdown.calc.Calculator;

import java.util.List;

public class RpnCalculator implements Calculator {
    private static final CalculationMode MODE = CalculationMode.RPN;

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