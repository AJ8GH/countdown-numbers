package aj.countdown.domain;

import aj.countdown.generator.Operator;

public class Calculation {

    private StringBuilder solution;

    private int result;

    public Calculation(Operator operator, int x, int y) {
        this.result = operator.apply(x, y);
        this.solution = new StringBuilder("(")
                .append(x)
                .append(operator)
                .append(y)
                .append(")");
    }

    public Calculation(Operator operator, Calculation calculationX, Calculation calculationY) {
        this.result = operator.apply(calculationX.result, calculationY.result);
        this.solution = new StringBuilder("(")
                .append(calculationX)
                .append(operator)
                .append(calculationY)
                .append(")");
    }

    public Calculation(Operator operator, int x, Calculation calculationY) {
        this.result = operator.apply(x, calculationY.result);
        this.solution = new StringBuilder("(")
                .append(x)
                .append(operator)
                .append(calculationY)
                .append(")");
    }

    public Calculation(Operator operator, Calculation calculationX, int y) {
        this.result = operator.apply(calculationX.result, y);
        this.solution = new StringBuilder("(")
                .append(calculationX)
                .append(operator)
                .append(y)
                .append(")");
    }

    public Calculation(int x) {
        this.result = x;
        this.solution = new StringBuilder(String.valueOf(result));
    }

    public void calculate(Operator operator, Calculation calculation) {
        this.result = operator.apply(result, calculation.result);
        this.solution = new StringBuilder("(")
                .append(this)
                .append(operator)
                .append(calculation)
                .append(")");
    }

    public int getResult() {
        return result;
    }

    public String getSolution() {
        return solution.toString();
    }

    @Override
    public String toString() {
        return solution.toString();
    }
}
