package io.github.aj8gh.countdown.util.calculator;

import io.github.aj8gh.countdown.util.calculator.impl.IntermediateCalculator;
import io.github.aj8gh.countdown.util.calculator.impl.SequentialCalculator;
import io.github.aj8gh.countdown.util.serialisation.RpnParser;
import io.github.aj8gh.countdown.util.timer.Timer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CalculatorTest {
    private static final Timer TIMER = new Timer();
    private static final RpnParser RPN_PARSER = new RpnParser();
    private final AtomicInteger attempts = new AtomicInteger(1);
    private Calculation result;

    @BeforeEach
    void setUp() {
        TIMER.start();
    }

    @AfterEach
    void tearDown() {
        TIMER.stop();
        System.out.println("==================");
        System.out.println(TIMER.getLastTime() + " ms");
        System.out.println(result.getSolution());
        System.out.println(result.getValue());
        System.out.println(result.getRpn());
        System.out.println("Attempts: " + attempts);
        System.out.println("==================");
        TIMER.reset();
        attempts.set(1);
    }

    @Order(0)
    @ParameterizedTest
    @MethodSource(value = "getInputs")
    void calculateSolution_Sequential(List<Integer> numbers) {
        var calculator = new SequentialCalculator();
        numbers = new ArrayList<>(numbers);
        var target = numbers.remove(numbers.size() - 1);

        result = calculator.calculateSolution(numbers, target);
        while (result.getValue() != target) {
            result = calculator.calculateSolution(numbers, target);
            attempts.incrementAndGet();
        }
        assertEquals(target, result.getValue());
        assertEquals(target, RPN_PARSER.parse(result.getRpn()));
    }

    @Order(1)
    @ParameterizedTest
    @MethodSource(value = { "getInputs", "getHardestInput" })
    void calculateSolution_Intermediate(List<Integer> numbers) {
        var calculator = new IntermediateCalculator();
        numbers = new ArrayList<>(numbers);
        var target = numbers.remove(numbers.size() - 1);

        result = calculator.calculateSolution(numbers, target);
        while (result.getValue() != target) {
            result = calculator.calculateSolution(numbers, target);
            attempts.incrementAndGet();
        }
        assertEquals(target, result.getValue());
        assertEquals(target, RPN_PARSER.parse(result.getRpn()));
    }

    private static List<List<Integer>> getInputs() {
        return List.of(
                List.of(1, 2, 3, 4, 5, 6, 100),
                List.of(50, 25, 75, 2, 1, 100, 199)
        );
    }

    private static List<List<Integer>> getHardestInput() {
        return List.of(
                List.of(75, 5, 25, 6, 50, 100, 426)
        );
    }
}
