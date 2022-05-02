package io.github.aj8gh.countdown.calc.impl;

import io.github.aj8gh.countdown.calc.Calculation;
import io.github.aj8gh.countdown.calc.Calculator;
import io.github.aj8gh.countdown.calc.Operator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RecursiveCalculator implements Calculator {
    private static final CalculationMode MODE = CalculationMode.RECURSIVE;
    private static final Logger LOG = LoggerFactory.getLogger(RecursiveCalculator.class);
    private int recursions;
    private int target;
    private Calculation result;

    @Override
    public Calculation calculateTarget(List<Integer> numbers) {
        return null;
    }

    @Override
    public Calculation calculateSolution(List<Integer> numbers, int target) {
        this.target = target;
        var calcs = new Calculation[numbers.size()];
        for (int i = 0; i < numbers.size(); i++) calcs[i] = new Calculation(numbers.get(i));
        if (calculateRecursively(calcs, calcs.length)) {
            LOG.info("Success:\n{}\nrecursions: {}", result, recursions);
            return resetAndGetResult();
        } else {
            LOG.warn("FAIL :(");
        }
        return null;
    }

    @Override
    public CalculationMode getMode() {
        return MODE;
    }

    private boolean calculateRecursively(Calculation[] numbers, int inputSize) {
        recursions++;
        for (int i = 0; i < inputSize; i++) {
            if (isSolved(numbers[i], target)) return true;
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
        this.recursions = 0;
        return finalResult;
    }

    private boolean isSolvedRecursively(Calculation[] numbers, int i, int j, int inputSize) {
        for (Operator operation : OPERATORS.values()) {
            var saveI = numbers[i];
            var saveJ = numbers[j];
            var res = new Calculation(saveI).calculate(operation, new Calculation(saveJ));

            if (res.getValue() != 0) {
                numbers[i] = res;
                numbers[j] = numbers[inputSize - 1];

                if (calculateRecursively(numbers, inputSize - 1)) {
                    if (result == null) result = res;
                    return true;
                }
                numbers[i] = saveI;
                numbers[j] = saveJ;
            }
        }
        return false;
    }
}
