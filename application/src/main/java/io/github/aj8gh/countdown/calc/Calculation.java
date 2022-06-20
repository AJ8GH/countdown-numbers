package io.github.aj8gh.countdown.calc;


public class Calculation {
    private static final String LEFT_PARENTHESIS = "(";
    private static final String RIGHT_PARENTHESIS = ")";

    private StringBuilder solution;
    private int value;
    private int numbers = 1;

    public Calculation(int x) {
        this.value = x;
        this.solution = new StringBuilder(String.valueOf(x));
    }

    public Calculation(Calculation calculation) {
        this.value = calculation.getValue();
        this.solution = calculation.solution;
        this.numbers = calculation.numbers;
    }

    public Calculation calculate(Operator operator, Calculation calculation) {
        apply(operator, calculation);
        return this;
    }

    public Calculation calculate(Operator operator, int number) {
        apply(operator, new Calculation(number));
        return this;
    }

    private void apply(Operator operator, Calculation calculation) {
        int result;
        Calculation first = this;
        Calculation second = calculation;
        if (value > calculation.getValue()) {
            result = operator.apply(value, calculation.getValue());
        } else {
            result = operator.apply(calculation.getValue(), value);
            first = calculation;
            second = this;
        }
        if (isValid(result, calculation)) {
            buildSolution(first, operator, second);
            this.value = result;
            this.numbers += calculation.numbers;
        }
    }

    private boolean isValid(int result, Calculation calculation) {
        return result != 0 && result != value && result != calculation.getValue();
    }

    private void buildSolution(Calculation first, Operator op, Calculation second) {
        this.solution = new StringBuilder(LEFT_PARENTHESIS)
                .append(first)
                .append(op)
                .append(second)
                .append(RIGHT_PARENTHESIS);
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return solution.toString();
    }
}
