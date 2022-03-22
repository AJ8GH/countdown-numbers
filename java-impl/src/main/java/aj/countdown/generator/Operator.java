package aj.countdown.generator;

import java.util.function.IntBinaryOperator;

public enum Operator {
    ADD("+", Integer::sum),
    SUBTRACT("-", (x, y) -> x - y),
    MULTIPLY("*", (x, y) -> x * y),
    DIVIDE("/", (x, y) -> x / y);

    private final String symbol;
    private final IntBinaryOperator op;

    Operator(String symbol, IntBinaryOperator op) {
        this.symbol = symbol;
        this.op = op;
    }

    public int apply(int x, int y) {
        return op.applyAsInt(x, y);
    }

    @Override
    public String toString() {
        return " " + symbol + " ";
    }
}
