package io.github.aj8gh.countdown.util.calculator.impl;

import io.github.aj8gh.countdown.util.calculator.Calculation;
import io.github.aj8gh.countdown.util.calculator.Calculator;
import io.github.aj8gh.countdown.util.calculator.Operator;
import it.unimi.dsi.util.XoRoShiRo128PlusRandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.MIXED;
import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.SEQUENTIAL;
import static io.github.aj8gh.countdown.util.calculator.Operator.DIVIDE;
import static java.util.Collections.shuffle;

public class CalculatorImpl implements Calculator {
    private static final XoRoShiRo128PlusRandom RANDOM = new XoRoShiRo128PlusRandom();
    private static final List<Operator> OPERATORS = Arrays.asList(Operator.values());
    private static final CalculationMode DEFAULT_MODE = SEQUENTIAL;

    private CalculationMode mode = DEFAULT_MODE;

    public Calculation calculate(List<Integer> numbers) {

        List<Calculation> calculations = numbers.stream()
                .map(Calculation::new)
                .toList();
        if (mode.equals(SEQUENTIAL)) {
            return calculateRunning(calculations, 0);
        }
        return calculateTarget(calculations);
    }

    public Calculation calculateSolution(List<Integer> numbers) {
        int target = numbers.remove(numbers.size() - 1);
        List<Calculation> calculations = numbers.stream()
                .map(Calculation::new)
                .toList();

        return mode.equals(SEQUENTIAL) ?
                calculateRunning(calculations, target) :
                calculateTarget(calculations, target);
    }

    public CalculationMode getMode() {
        return mode;
    }

    public void setMode(CalculationMode mode) {
        this.mode = mode;
    }

    private Calculation calculateRunning(List<Calculation> calculations, int target) {
        calculations = new ArrayList<>(calculations);
        shuffle(calculations);
        var x = calculations.get(0);
        for (int i = 1; i < calculations.size(); i++) {
            if (target > 0 && x.getValue() == target) return x;
            x = calculate(x, calculations.get(i));
        }
        return x;
    }

    private Calculation calculateTarget(List<Calculation> inputs) {
        return calculateTarget(inputs, 0);
    }

    private Calculation calculateTarget(List<Calculation> inputs, int target) {
        List<Calculation> results = new ArrayList<>();
        for (int i = 0; i < inputs.size(); i++) {
            Calculation x = inputs.get(i);
            Calculation y = (takeFromInput(i, inputs)) ? inputs.get(++i) :
                    results.remove(RANDOM.nextInt(results.size()));
            Calculation result = calculate(x, y);
            if (target > 0 && result.getValue() == target) return result;
            results.add(result);
        }
        return results.size() == 1 ? results.get(0) : calculateTarget(results);
    }

    private Calculation calculate(Calculation x, Calculation y) {
        Calculation result = null;
        while (result == null) {
            result = getResult(getOperator(), x, y);
        }
        return result;
    }

    private Calculation getResult(Operator operator, Calculation x, Calculation y) {
        if (operator.apply(x.getValue(), y.getValue()) == 0) return null;
        if (operator.equals(DIVIDE)) {
            if (x.getValue() % y.getValue() == 0) {
                return Calculation.calculate(x, operator, y);
            } else if (y.getValue() % x.getValue() == 0) {
                return Calculation.calculate(y, operator, x);
            }
            return null;
        }
        return Calculation.calculate(x, operator, y);
    }

    private boolean takeFromInput(int i, List<Calculation> inputs) {
        if (i == 0 || (6 - i <= inputs.size() - 1)) return true;
        if (mode.equals(MIXED)) return RANDOM.nextBoolean();
        return mode.equals(SEQUENTIAL);
    }

    private Operator getOperator() {
        return OPERATORS.get(RANDOM.nextInt(OPERATORS.size()));
    }
}
