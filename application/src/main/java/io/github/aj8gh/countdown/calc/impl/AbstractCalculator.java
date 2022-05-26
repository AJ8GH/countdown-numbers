package io.github.aj8gh.countdown.calc.impl;

import io.github.aj8gh.countdown.calc.Calculation;
import io.github.aj8gh.countdown.calc.Calculator;
import io.github.aj8gh.countdown.calc.Operator;

import java.util.Random;

public abstract class AbstractCalculator implements Calculator {
    protected static final Random RANDOM = new Random();
    static final Operator[] OPERATORS = Operator.values();

    Calculation doCalculation(Calculation calculation, Integer number) {
        var value = calculation.getValue();
        var result = calculation.calculate(getOperator(), number);
        while (calculation.getValue() == value) {
            result = calculation.calculate(getOperator(), number);
        }
        return result;
    }

    Calculation doCalculation(Calculation first, Calculation second) {
        var value = first.getValue();
        var result = first.calculate(getOperator(), second);
        while (first.getValue() == value) {
            result = first.calculate(getOperator(), second);
        }
        return result;
    }

    Operator getOperator() {
        return OPERATORS[RANDOM.nextInt(OPERATORS.length)];
    }
}
