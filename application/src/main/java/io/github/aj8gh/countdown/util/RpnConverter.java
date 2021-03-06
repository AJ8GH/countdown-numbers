package io.github.aj8gh.countdown.util;

import static java.util.stream.Collectors.toSet;

import io.github.aj8gh.countdown.calc.Operator;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class RpnConverter {
    private static final String LEFT_PARENTHESIS = "(";
    private static final String RIGHT_PARENTHESIS = ")";
    private static final String SPACE = " ";
    private static final String COMMA = ",";
    private static final Set<String> OPERATORS = EnumSet.allOf(Operator.class)
            .stream()
            .map(Operator::symbol)
            .collect(toSet());

    private final Deque<String> dequeue = new ArrayDeque<>();
    private final List<String> rpnBuilder = new LinkedList<>();

    public String convert(String solution) {
        for (String element : solution.split(SPACE)) {
            convertElement(element);
        }
        addRemainingOperators();
        return buildAndReturnRpn();
    }

    private void convertElement(String element) {
        if (isOperator(element) || isLeftParenthesis(element)) {
            dequeue.push(element);
        } else if (isRightParenthesis(element)) {
            addUntilLeftParenthesis();
        } else {
            rpnBuilder.add(element);
        }
    }

    private boolean isOperator(String element) {
        return OPERATORS.contains(element);
    }

    private boolean isLeftParenthesis(String element) {
        return element.equals(LEFT_PARENTHESIS);
    }

    private boolean isRightParenthesis(String element) {
        return element.equals(RIGHT_PARENTHESIS);
    }

    private void addUntilLeftParenthesis() {
        while (isNotLeftParenthesisNext()) {
            rpnBuilder.add(dequeue.pop());
        }
        dequeue.pop();
    }

    private boolean isNotLeftParenthesisNext() {
        return !dequeue.isEmpty() && !isLeftParenthesis(dequeue.peek());
    }

    private void addRemainingOperators() {
        while (!dequeue.isEmpty()) {
            rpnBuilder.add(dequeue.pop());
        }
    }

    private String buildAndReturnRpn() {
        var rpn = String.join(COMMA, rpnBuilder);
        dequeue.clear();
        rpnBuilder.clear();
        return rpn;
    }
}
