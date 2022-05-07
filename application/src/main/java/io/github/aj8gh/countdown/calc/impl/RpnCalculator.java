package io.github.aj8gh.countdown.calc.impl;

import io.github.aj8gh.countdown.calc.Calculation;
import io.github.aj8gh.countdown.calc.Operator;
import io.github.aj8gh.countdown.calc.rpn.RpnParser;

import java.util.List;

public class RpnCalculator extends AbstractCalculator {
    private static final CalculationMode MODE = CalculationMode.RPN;
    private static final RpnParser RPN_PARSER = new RpnParser();

    @Override
    public Calculation calculateTarget(List<Integer> numbers) {
        return null;
    }

    @Override
    public Calculation calculateSolution(List<Integer> numbers, int target) {
        var elements = new String[10];
        for (int i = 0; i < numbers.size(); i++) {
            elements[i] = String.valueOf(numbers.get(i));
        }
        for (int i = numbers.size(); i < Operator.values().length + numbers.size(); i++) {
            elements[i] = Operator.values()[i % Operator.values().length].symbol();
        }

        int n = numbers.size();
        int[] indexes = new int[n];
        for (int i = 0; i < n; i++) {
            indexes[i] = 0;
        }

        var res = checkResult(elements);
        if (res.getValue() == target) return res;
        int i = 0;

        while (i < n) {
            if (indexes[i] < i) {
                swap(elements, i % 2 == 0 ?  0: indexes[i], i);
                res = checkResult(elements);
                if (res.getValue() == target) return res;
                indexes[i]++;
                i = 0;
            }
            else {
                indexes[i] = 0;
                i++;
            }
        }
        return null;
    }

    @Override
    public CalculationMode getMode() {
        return MODE;
    }

    private Calculation checkResult(String[] rpn) {
        return new Calculation(RPN_PARSER.parse(String.join(",", rpn)));
    }

    private void swap(String[] input, int a, int b) {
        String tmp = input[a];
        input[a] = input[b];
        input[b] = tmp;
    }
}
