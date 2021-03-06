package io.github.aj8gh.countdown.calc;

import io.github.aj8gh.countdown.calc.impl.IntermediateCalculator;
import io.github.aj8gh.countdown.calc.impl.RecursiveCalculator;
import io.github.aj8gh.countdown.calc.impl.SequentialCalculator;
import io.github.aj8gh.countdown.util.RpnConverter;
import io.github.aj8gh.countdown.util.RpnParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CalculatorTest {
    private static final RpnParser RPN_PARSER = new RpnParser();
    private static final RpnConverter RPN_CONVERTER = new RpnConverter();
    private final AtomicInteger attempts = new AtomicInteger(1);
    private Calculation result;

    @AfterEach
    void tearDown() {
        System.out.println("==================");
        System.out.println(result.getValue());
        System.out.println(RPN_CONVERTER.convert(result.toString()));
        System.out.println("Attempts: " + attempts);
        System.out.println("==================");
        attempts.set(1);
    }

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
        assertEquals(target, RPN_PARSER.parse(RPN_CONVERTER.convert(result.toString())));
    }

    @ParameterizedTest
    @MethodSource(value = { "getInputs", "getDifficultInputs"})
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
        assertEquals(target, RPN_PARSER.parse(RPN_CONVERTER.convert(result.toString())));
    }

    @ParameterizedTest
    @MethodSource(value = { "getInputs", "getDifficultInputs" })
    void calculateSolution_Recursive(List<Integer> numbers) {
        var calculator = new RecursiveCalculator();
        numbers = new ArrayList<>(numbers);
        var target = numbers.remove(numbers.size() - 1);

        result = calculator.calculateSolution(numbers, target);
        assertNotNull(result, "Expected GenResult to not be null");
        assertEquals(target, result.getValue());
        assertEquals(target, RPN_PARSER.parse(RPN_CONVERTER.convert(result.toString())));
    }

    private static List<List<Integer>> getInputs() {
        return List.of(
                List.of(25, 100, 50, 5, 6, 6, 236),
                List.of(1, 2, 3, 4, 5, 6, 100),
                List.of(50, 25, 75, 2, 1, 100, 199),
                List.of(6, 100, 4, 6, 8, 2, 752),
                List.of(75, 25, 10, 1, 10, 2, 101),
                List.of(100, 50, 10, 3, 6, 3, 319),
                List.of(50, 100, 3, 10, 1, 4, 166),
                List.of(75, 50, 3, 10, 5, 4, 331),
                List.of(100, 75, 9, 9, 3, 4, 163),
                List.of(25, 100, 2, 9, 7, 5, 253),
                List.of(100, 75, 2, 9, 2, 8, 269),
                List.of(25, 100, 2, 6, 10, 4, 506),
                List.of(75, 100, 8, 7, 1, 6, 566),
                List.of(50, 25, 1, 4, 10, 1, 429),
                List.of(25, 50, 75, 100, 3, 6, 952)
        );
    }

    private static List<List<Integer>> getDifficultInputs() {
        return List.of(
                List.of(75, 5, 25, 6, 50, 100, 426)
        );
    }
}
