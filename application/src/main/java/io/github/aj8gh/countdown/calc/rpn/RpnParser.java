package io.github.aj8gh.countdown.calc.rpn;

import io.github.aj8gh.countdown.calc.Operator;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumSet;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

public class RpnParser {
    private static final String COMMA = ",";
    private static final Map<String, Operator> OPERATORS = EnumSet.allOf(Operator.class)
            .stream().collect(toMap(Operator::symbol, Function.identity()));

    private final Deque<String> numberStack = new ArrayDeque<>();

    public int parse(String rpn) {
        var elements = rpn.split(COMMA);
        for (String e : elements) {
            numberStack.push(isOperator(e) ? calculate(e) : e);
        }
        return Integer.parseInt(numberStack.pop());
    }

    private String calculate(String operator) {
        var second = Integer.parseInt(numberStack.pop());
        var first = Integer.parseInt(numberStack.pop());
        return String.valueOf(OPERATORS.get(operator).apply(first, second));
    }

    private boolean isOperator(String element) {
        return OPERATORS.containsKey(element);
    }
}
