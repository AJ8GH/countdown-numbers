package aj.countdown.domain;

import aj.countdown.generator.Operator;

public class Calculation {
    private final Operator operator;
    private final int result;
    private Calculation calculationX;
    private Calculation calculationY;
    private int x;
    private int y;

    private StringBuilder solution;

    public Calculation(Operator operator, int x, int y) {
        this.x = x;
        this.y = y;
        this.operator = operator;
        this.result = operator.apply(x, y);
        this.solution = new StringBuilder("(")
                .append(x).append(operator).append(y).append(")");
    }

    public Calculation(Operator operator, Calculation calculationX, Calculation calculationY) {
        this.operator = operator;
        this.calculationX = calculationX;
        this.calculationY = calculationY;
        this.result = operator.apply(calculationX.result, calculationY.result);
        this.solution = new StringBuilder("(")
                .append(calculationX).append(operator).append(calculationY).append(")");
    }

    public Calculation(Operator operator, int x, Calculation calculationY) {
        this.operator = operator;
        this.x = x;
        this.calculationY = calculationY;
        this.result = operator.apply(x, calculationY.result);
        this.solution = new StringBuilder("(")
                .append(x).append(operator).append(calculationY).append(")");
    }

    public Calculation(Operator operator, Calculation calculationX, int y) {
        this.operator = operator;
        this.calculationX = calculationX;
        this.y = y;
        this.result = operator.apply(calculationX.result, y);
        this.solution = new StringBuilder("(")
                .append(calculationX).append(operator).append(y).append(")");
    }

    public Calculation(int x) {
        this.x = x;
        this.result = x;
        this.solution = new StringBuilder(x);
        this.operator = null;
    }

    public int getResult() {
        return result;
    }

    public String getSolution() {
        return toString();
    }

    @Override
    public String toString() {
        return solution.toString();
    }
}
