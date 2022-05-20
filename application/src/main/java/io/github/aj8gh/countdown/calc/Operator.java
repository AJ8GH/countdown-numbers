package io.github.aj8gh.countdown.calc;

import java.util.function.IntBinaryOperator;

public enum Operator {
    ADD("+", Integer::sum),
    SUBTRACT("-", (x, y) -> Math.max(x - y, 0)),
    MULTIPLY("*", (x, y) -> x * y),
    DIVIDE("/", (x, y) -> y != 0 && x % y == 0 ? x / y : 0);

    private final String symbol;
    private final IntBinaryOperator op;

    Operator(String symbol, IntBinaryOperator op) {
        this.symbol = symbol;
        this.op = op;
    }

    public int apply(int first, int second) {
        return op.applyAsInt(first, second);
    }

    public String symbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return " " + symbol + " ";
    }
}
