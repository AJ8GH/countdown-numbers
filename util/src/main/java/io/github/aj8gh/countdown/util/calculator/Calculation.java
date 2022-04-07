package io.github.aj8gh.countdown.util.calculator;

import io.github.aj8gh.countdown.util.serialisation.RpnConverter;

public class Calculation implements Comparable<Calculation> {
    private static final RpnConverter RPN_CONVERTER = new RpnConverter();

    private StringBuilder solution;
    private int value;
    private String rpn;

    public Calculation(int x) {
        this.value = x;
        this.solution = new StringBuilder(String.valueOf(x));
    }

    public Calculation calculate(Operator operator, Calculation calculation) {
        this.value = operator.apply(value, calculation.value);
        this.solution = buildSolution(operator, calculation);
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

    @Override
    public int compareTo(Calculation other) {
        return Integer.compare(value, other.getValue());
    }

    private StringBuilder buildSolution(Operator op, Calculation calc) {
        return new StringBuilder("(")
                .append(this)
                .append(op)
                .append(calc)
                .append(")");
    }
}
