package io.github.aj8gh.countdown.util.calculator;

import java.util.function.IntBinaryOperator;

public enum Operator {
    ADD("+", Integer::sum),
    SUBTRACT("-", (left, right) -> left - right),
    MULTIPLY("*", (left, right) -> left * right),
    DIVIDE("/", (left, right) -> left / right);

    private final String symbol;
    private final IntBinaryOperator op;

    Operator(String symbol, IntBinaryOperator op) {
        this.symbol = symbol;
        this.op = op;
    }

    public int apply(int left, int right) {
        return op.applyAsInt(left, right);
    }

    public String symbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return " " + symbol + " ";
    }
}
