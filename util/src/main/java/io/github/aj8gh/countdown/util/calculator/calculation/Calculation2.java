package io.github.aj8gh.countdown.util.calculator.calculation;

import io.github.aj8gh.countdown.util.calculator.Operator;
import io.github.aj8gh.countdown.util.serialisation.RpnConverter;

public class Calculation2 implements Calculation {
    private static final String LEFT_PARENTHESIS = "(";
    private static final String RIGHT_PARENTHESIS = ")";
    private static final RpnConverter RPN_CONVERTER = new RpnConverter();

    private StringBuilder solution;
    private int value;
    private String rpn;

    public Calculation2(int x) {
        this.value = x;
        this.solution = new StringBuilder(String.valueOf(x));
    }

    public Calculation calculate(Operator operator, Calculation calculation) {
        this.value = apply(operator, calculation);
        return this;
    }

    public Calculation calculate(Operator operator, int number) {
        this.value = apply(operator, new Calculation2(number));
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

    private int apply(Operator operator, Calculation calculation) {
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
        if (result != 0) buildSolution(first, operator, second);
        return result;
    }

    private void buildSolution(Calculation first, Operator op, Calculation second) {
        this.solution = new StringBuilder(LEFT_PARENTHESIS)
                .append(first)
                .append(op)
                .append(second)
                .append(RIGHT_PARENTHESIS);
    }
}
