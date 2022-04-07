package io.github.aj8gh.countdown.util.calculator;

import io.github.aj8gh.countdown.util.calculator.impl.CalculatorImpl;
import io.github.aj8gh.countdown.util.calculator.impl.SequentialCalculator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculatorTest {
    @Test
    void calculateImpl() {
        var calculator = new CalculatorImpl();
        var target = 100;
        var input = List.of(1, 2, 3, 4, 5, 6, target);

        var result = calculator.calculate(input);
        while (result.getValue() != target) {
            result = calculator.calculate(input);
        }

        assertEquals(target, result.getValue());
    }

    @Test
    void calculateSequential() {
        System.out.println(EnumSet.allOf(Operator.class).stream().map(Enum::ordinal).toList());
        var calculator = new SequentialCalculator();
        var target = 100;
        var input = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, target));

        var result = calculator.calculate(input);
        while (result.getValue() != target) {
            result = calculator.calculate(input);
        }

        assertEquals(target, result.getValue());
    }
}
