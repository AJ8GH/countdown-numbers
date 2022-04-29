package io.github.aj8gh.countdown.util.calculator.impl;

import io.github.aj8gh.countdown.util.calculator.Calculation;
import io.github.aj8gh.countdown.util.calculator.Calculator;
import io.github.aj8gh.countdown.util.calculator.Operator;

import java.util.ArrayList;
import java.util.List;

import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.RECURSIVE;

public class RecursiveCalculator implements Calculator {
    private static final CalculationMode MODE = RECURSIVE;

    @Override
    public Calculation calculateTarget(List<Integer> numbers) {
        return null;
    }

    @Override
    public Calculation calculateSolution(List<Integer> numbers, int target) {
        var calculations = new ArrayList<>(numbers.stream().map(Calculation::new).toList());
        return calculateRecursively(calculations, calculations.size(), target);
    }

    @Override
    public CalculationMode getMode() {
        return MODE;
    }

    private Calculation calculateRecursively(List<Calculation> calculations,
                                             int inputSize, int target) {
        for (int i = 0; i < inputSize; i++) {
            var check = calculations.get(i);
            if (check.getValue() == target) return check;

            for (int j = i + 1; j < inputSize; j++) {
                for (Operator operator : OPERATORS.values()) {
                    var first = calculations.get(i);
                    var second = calculations.get(j);
                    var result = first.calculate(operator, second);

                    if (result.getValue() != first.getValue() && result.getValue() != second.getValue()) {
                        var saveI = calculations.get(i);
                        var saveJ = calculations.get(j);
                        calculations.set(i, result);
                        calculations.set(j, calculations.get(inputSize - 1));

                        if (calculateRecursively(calculations, inputSize - 1, target) != null) {
                            return result;
                        }
                        calculations.set(i, saveI);
                        calculations.set(j, saveJ);
                    }
                }
            }
        }
        return null;
    }
}
