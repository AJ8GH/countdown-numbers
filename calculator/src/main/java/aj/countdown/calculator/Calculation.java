package aj.countdown.calculator;

public class Calculation {

    private StringBuilder solution;

    private int result;

    public Calculation(int x) {
        this.result = x;
        this.solution = new StringBuilder(String.valueOf(result));
    }

    public Calculation calculate(Operator operator, Calculation calculation) {
        this.result = operator.apply(result, calculation.result);
        this.solution = new StringBuilder("(")
                .append(this)
                .append(operator)
                .append(calculation)
                .append(")");
        return this;
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
