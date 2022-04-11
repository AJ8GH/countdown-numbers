package io.github.aj8gh.countdown.util.serialisation;

import io.github.aj8gh.countdown.util.calculator.Operator;

import java.util.EnumSet;
import java.util.Map;
import java.util.Stack;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

public class RpnParser {
    private static final String COMMA = ",";
    private static final Map<String, Operator> OPERATORS = EnumSet.allOf(Operator.class)
            .stream().collect(toMap(Operator::symbol, Function.identity()));

    private final Stack<String> numberStack = new Stack<>();

    public int parse(String rpn) {
        var elements = rpn.split(COMMA);
        for (String element : elements) {
            if (OPERATORS.containsKey(element)) {
                var second = numberStack.pop();
                var first = numberStack.pop();
                numberStack.add(String.valueOf(calculate(first, element, second)));
            } else {
                numberStack.add(element);
            }
        }
        return Integer.parseInt(numberStack.pop());
    }

    private int calculate(String first, String operator, String second) {
        var op = OPERATORS.get(operator);
        return op.apply(Integer.parseInt(first), Integer.parseInt(second));
    }
}
