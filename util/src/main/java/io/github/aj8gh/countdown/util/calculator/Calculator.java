package io.github.aj8gh.countdown.util.calculator;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

public interface Calculator {
    Map<Integer, Operator> OPERATORS = Arrays.stream(Operator.values())
            .collect(toMap(Enum::ordinal, Function.identity()));

    enum CalculationMode {
        SEQUENTIAL, INTERMEDIATE, MIXED, RECURSIVE
    }

    Calculation calculate(List<Integer> numbers);

    CalculationMode getMode();

    default Calculation calculateUntilValid(Calculation left, Calculation right, Operator operator) {
        var result = calculate(left, right, operator);
        while (result.getValue() == 0) {
            result = calculate(left, right, operator);
        }
        return result;
    }

    default Calculation calculate(Calculation left, Calculation right, Operator operator) {
        return operator.isCommutative() ?
                left.calculate(operator, right):
                calculateNonCommutative(left, right, operator);
    }

    default Calculation calculateNonCommutative(Calculation left,
                                                Calculation right,
                                                Operator operator) {
        return left.compareTo(right) > 0 ?
                left.calculate(operator, right) :
                right.calculate(operator, left);
    }


}
