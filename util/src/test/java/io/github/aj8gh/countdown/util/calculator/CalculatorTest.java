package io.github.aj8gh.countdown.util.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculatorTest {
    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @Test
    void calculate() {
        var target = 100;
        var input = List.of(1, 2, 3, 4, 5, 6, target);

        var result = calculator.calculate(input);
        while (result.getResult() != target) {
            result = calculator.calculate(input);
        }

        assertEquals(100, result.getResult());
    }
}
