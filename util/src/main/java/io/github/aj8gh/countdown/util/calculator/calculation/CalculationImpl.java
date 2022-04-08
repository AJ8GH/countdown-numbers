package io.github.aj8gh.countdown.util.calculator.calculation;

import io.github.aj8gh.countdown.util.calculator.Operator;
import io.github.aj8gh.countdown.util.serialisation.RpnConverter;

public class CalculationImpl implements Calculation {
    private static final String LEFT_PARENTHESIS = "(";
    private static final String RIGHT_PARENTHESIS = ")";
    private static final RpnConverter RPN_CONVERTER = new RpnConverter();

    private StringBuilder solution;
    private int value;
    private String rpn;

    public CalculationImpl(int x) {
        this.value = x;
        this.solution = new StringBuilder(String.valueOf(x));
    }

    @Override
    public Calculation calculate(Operator operator, Calculation calculation) {
        this.value = operator.apply(value, calculation.getValue());
        this.solution = buildSolution(operator, calculation);
        return this;
    }

    @Override
    public Calculation calculate(Operator operator, int number) {
        this.value = operator.apply(value, number);
        this.solution = buildSolution(operator, new CalculationImpl(number));
        return this;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public String getSolution() {
        return solution.toString();
    }

    @Override
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

    private StringBuilder buildSolution(Operator op, Calculation calc) {
        return new StringBuilder(LEFT_PARENTHESIS)
                .append(this)
                .append(op)
                .append(calc)
                .append(RIGHT_PARENTHESIS);
    }
}
