package io.github.aj8gh.countdown.calc;

import java.util.function.IntBinaryOperator;

public enum Operator {
    ADD("+", Integer::sum, true),
    SUBTRACT("-", (x, y) -> Math.max(x - y, 0), false),
    MULTIPLY("*", (x, y) -> x * y, true),
    DIVIDE("/", (x, y) -> y != 0 && x % y == 0 ? x / y : 0, false);

    private final String symbol;
    private final IntBinaryOperator op;
    private final boolean commutative;

    Operator(String symbol, IntBinaryOperator op, boolean commutative) {
        this.symbol = symbol;
        this.op = op;
        this.commutative = commutative;
    }

    public int apply(int first, int second) {
        return op.applyAsInt(first, second);
    }

    public String symbol() {
        return symbol;
    }

    public boolean isCommutative() {
        return commutative;
    }

    @Override
    public String toString() {
        return " " + symbol + " ";
    }
}