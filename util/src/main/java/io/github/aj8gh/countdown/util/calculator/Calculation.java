package io.github.aj8gh.countdown.util.calculator;

import io.github.aj8gh.countdown.util.rpn.RpnConverter;

public class Calculation {
    private static final String LEFT_PARENTHESIS = "(";
    private static final String RIGHT_PARENTHESIS = ")";
    private static final RpnConverter RPN_CONVERTER = new RpnConverter();

    private StringBuilder solution;
    private int value;
    private String rpn;

    public Calculation(int x) {
        this.value = x;
        this.solution = new StringBuilder(String.valueOf(x));
    }

    public Calculation calculate(Operator operator, Calculation calculation) {
        apply(operator, calculation);
        return this;
    }

    public Calculation calculate(Operator operator, int number) {
        apply(operator, new Calculation(number));
        return this;
    }

    public int getValue() {
        return value;
    }

    public String getSolution() {
        return solution.toString();
    }

    public String getRpn() {
        if (rpn == null) {
            this.rpn = RPN_CONVERTER.convert(solution.toString());
        }
        return rpn;
    }

    @Override
    public String toString() {
        return solution.toString();
    }

    private void apply(Operator operator, Calculation calculation) {
        int result;
        Calculation first = this;
        Calculation second = calculation;
        if (operator.isCommutative() || value > calculation.getValue()) {
            result = operator.apply(value, calculation.getValue());
        } else {
            result = operator.apply(calculation.getValue(), value);
            first = calculation;
            second = this;
        }
        if (isValid(result, calculation)) {
            buildSolution(first, operator, second);
            this.value = result;
        }
    }

    private void buildSolution(Calculation first, Operator op, Calculation second) {
        this.solution = new StringBuilder(LEFT_PARENTHESIS)
                .append(first)
                .append(op)
                .append(second)
                .append(RIGHT_PARENTHESIS);
    }

    private boolean isValid(int result, Calculation calculation) {
        return result != 0 && result != value && result != calculation.getValue();
    }
}
