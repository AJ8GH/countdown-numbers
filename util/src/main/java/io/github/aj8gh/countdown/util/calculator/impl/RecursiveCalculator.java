package io.github.aj8gh.countdown.util.calculator.impl;

import io.github.aj8gh.countdown.util.calculator.Calculation;
import io.github.aj8gh.countdown.util.calculator.Calculator;
import io.github.aj8gh.countdown.util.calculator.Operator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.RECURSIVE;

public class RecursiveCalculator implements Calculator {
    private static final CalculationMode MODE = RECURSIVE;
    private static final Logger LOG = LoggerFactory.getLogger(RecursiveCalculator.class);
    private int recursions;
    private Calculation result;

    @Override
    public Calculation calculateTarget(List<Integer> numbers) {
        return null;
    }

    @Override
    public Calculation calculateSolution(List<Integer> numbers, int target) {
        var calcs = new Calculation[numbers.size()];
        for (int i = 0; i < numbers.size(); i++) calcs[i] = new Calculation(numbers.get(i));
        if (calculateRecursively(calcs, calcs.length, target)) {
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

    private boolean calculateRecursively(Calculation[] numbers, int inputSize, int target) {
        recursions++;
        for (int i = 0; i < inputSize; i++) {
            if (result != null) return true;
            if (numbers[i].getValue() == target) {
                this.result = numbers[i];
                return true;
            }

            for (int j = i + 1; j < inputSize; j++) {
                for (Operator operation : OPERATORS.values()) {
                    var res = new Calculation(operation.apply(numbers[i].getValue(), numbers[j].getValue()));

                    if (res.getValue() != 0) {
                        var saveI = numbers[i];
                        var saveJ = numbers[j];
                        res = new Calculation(saveI).calculate(operation, new Calculation(saveJ));
                        numbers[i] = res;
                        numbers[j] = numbers[inputSize - 1];

                        if (calculateRecursively(numbers, inputSize - 1, target)) {
                            if (result == null) result = res;
                            return true;
                        }
                        numbers[i] = saveI;
                        numbers[j] = saveJ;
                    }
                }
            }
        }
        return false;
    }

    private Calculation resetAndGetResult() {
        final var finalResult = result;
        this.result = null;
        this.recursions = 0;
        return finalResult;
    }
}
