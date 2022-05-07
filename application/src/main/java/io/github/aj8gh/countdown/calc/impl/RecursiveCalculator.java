package io.github.aj8gh.countdown.calc.impl;

import io.github.aj8gh.countdown.calc.Calculation;
import io.github.aj8gh.countdown.calc.Operator;

import java.util.ArrayList;
import java.util.List;

public class RecursiveCalculator extends AbstractCalculator {
    private static final CalculationMode MODE = CalculationMode.RECURSIVE;
    private int target;
    private Calculation result;

    @Override
    public Calculation calculateTarget(List<Integer> numbers) {
        return null;
    }

    @Override
    public Calculation calculateSolution(List<Integer> numbers, int target) {
        this.target = target;
        var calculations = new ArrayList<>(numbers.stream()
                .map(Calculation::new).toList());
        if (calculateRecursively(calculations, numbers.size())) {
            return resetAndGetResult();
        }
        return null;
    }

    @Override
    public CalculationMode getMode() {
        return MODE;
    }

    private boolean calculateRecursively(List<Calculation> numbers, int inputSize) {
        for (int i = 0; i < inputSize; i++) {
            if (isSolved(numbers.get(i), target)) return true;
            for (int j = i + 1; j < inputSize; j++) {
                if (isSolvedRecursively(numbers, i, j, inputSize)) return true;
            }
        }
        return false;
    }

    private boolean isSolved(Calculation c, int target) {
        if (result != null) return true;
        if (c.getValue() == target) {
            this.result = c;
            return true;
        }
        return false;
    }

    private Calculation resetAndGetResult() {
        final var finalResult = result;
        this.result = null;
        return finalResult;
    }

    private boolean isSolvedRecursively(List<Calculation> numbers, int i, int j, int inputSize) {
        for (Operator operation : OPERATORS.values()) {
            var saveI = numbers.get(i);
            var saveJ = numbers.get(j);
            var res = new Calculation(saveI).calculate(operation, new Calculation(saveJ));

            if (res.getValue() != 0) {
                numbers.set(i, res);
                numbers.set(j, numbers.get(inputSize - 1));

                if (calculateRecursively(numbers, inputSize - 1)) {
                    if (result == null) result = res;
                    return true;
                }
                numbers.set(i, saveI);
                numbers.set(j, saveJ);
            }
        }
        return false;
    }
}
