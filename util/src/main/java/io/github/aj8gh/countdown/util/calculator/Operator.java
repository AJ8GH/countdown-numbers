package io.github.aj8gh.countdown.util.calculator;

import java.util.function.IntBinaryOperator;

public enum Operator {
    ADD("+", Integer::sum, true),
    SUBTRACT("-", (x, y) -> x - y, false),
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

    public int apply(int x, int y) {
        return op.applyAsInt(x, y);
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
