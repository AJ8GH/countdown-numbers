package io.github.aj8gh.countdown.util.calculator;

import io.github.aj8gh.countdown.util.calculator.impl.CalculatorImpl;
import io.github.aj8gh.countdown.util.calculator.impl.SequentialCalculator;
import io.github.aj8gh.countdown.util.timer.Timer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
class CalculatorTest {
    private static final Timer TIMER = new Timer();

    @BeforeEach
    void setUp() {
        TIMER.start();
    }

    @AfterEach
    void tearDown() {
        TIMER.stop();
        System.out.println(TIMER.getLastTime());
        TIMER.reset();
    }

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
