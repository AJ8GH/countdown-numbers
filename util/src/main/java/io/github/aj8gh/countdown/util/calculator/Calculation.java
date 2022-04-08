package io.github.aj8gh.countdown.util.calculator;

import io.github.aj8gh.countdown.util.serialisation.RpnConverter;

public class Calculation implements Comparable<Calculation> {
    private static final String LEFT_PARENTHESIS = "(";
    private static final String RIGHT_PARENTHESIS = ")";
    private static final RpnConverter RPN_CONVERTER = new RpnConverter();

    private StringBuilder solution;
    private int value;
    private String rpn;

    public static Calculation calculate(int first, Operator operator, Calculation second) {
        var calculation = new Calculation(first);
        return calculation.calculate(operator, second);
    }

    public static Calculation calculate(int first, Operator operator, int second) {
        var calculation = new Calculation(first);
        return calculation.calculate(operator, second);
    }

    public static Calculation calculate(Calculation first, Operator operator, int second) {
        return first.calculate(operator, second);
    }

    public static Calculation calculate(Calculation first, Operator operator, Calculation second) {
        return first.calculate(operator, second);
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

    private Calculation(int x) {
        this.value = x;
        this.solution = new StringBuilder(String.valueOf(x));
    }

    private Calculation calculate(Operator operator, Calculation calculation) {
        this.value = operator.apply(value, calculation.value);
        this.solution = buildSolution(operator, calculation.toString());
        return this;
    }

    private Calculation calculate(Operator operator, int number) {
        this.value = operator.apply(value, number);
        this.solution = buildSolution(operator, String.valueOf(number));
        return this;
    }


    private StringBuilder buildSolution(Operator op, String calc) {
        return new StringBuilder(LEFT_PARENTHESIS)
                .append(this)
                .append(op)
                .append(calc)
                .append(RIGHT_PARENTHESIS);
    }
}
