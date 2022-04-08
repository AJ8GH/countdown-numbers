package io.github.aj8gh.countdown.util.calculator;

import io.github.aj8gh.countdown.util.calculator.calculation.Calculation;
import io.github.aj8gh.countdown.util.calculator.impl.CalculatorImpl;
import io.github.aj8gh.countdown.util.calculator.impl.SequentialCalculator;
import io.github.aj8gh.countdown.util.timer.Timer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CalculatorTest {
    private static final Timer TIMER = new Timer();
    private Calculation result;

    @BeforeEach
    void setUp() {
        TIMER.start();
    }

    @AfterEach
    void tearDown() {
        TIMER.stop();
        System.out.println("==================");
        System.out.println(TIMER.getLastTime());
        System.out.println(result.getSolution());
        System.out.println(result.getValue());
        System.out.println(result.getRpn());
        System.out.println("==================");
        TIMER.reset();
    }

    @Order(0)
    @Test
    void calculate_Impl() {
        var calculator = new CalculatorImpl();
        var target = 100;
        var input = List.of(1, 2, 3, 4, 5, 6, target);

        result = calculator.calculateSolution(input);
        while (result.getValue() != target) {
            result = calculator.calculateSolution(input);
        }
        assertEquals(target, result.getValue());
    }

    @Order(1)
    @Test
    void calculate_Impl_Harder() {
        var calculator = new CalculatorImpl();
        var target = 199;
        var input = new ArrayList<>(List.of(50, 25, 75, 2, 1, 100, 199));

        result = calculator.calculateSolution(input);
        while (result.getValue() != target) {
            result = calculator.calculateSolution(input);
        }
        assertEquals(target, result.getValue());
    }

    @Order(2)
    @Test
    void calculate_Sequential() {
        var calculator = new SequentialCalculator();
        var target = 100;
        var input = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, target));

        result = calculator.calculate(input);
        while (result.getValue() != target) {
            result = calculator.calculate(input);
        }
        assertEquals(target, result.getValue());
    }

    @Order(3)
    @Test
    void calculate_Sequential_Harder() {
        var calculator = new SequentialCalculator();
        var target = 199;
        var input = new ArrayList<>(List.of(50, 25, 75, 2, 1, 100, 199));

        result = calculator.calculate(input);
        while (result.getValue() != target) {
            result = calculator.calculate(input);
        }
        assertEquals(target, result.getValue());
    }
}
