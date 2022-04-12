package io.github.aj8gh.countdown.app;

import io.github.aj8gh.countdown.generator.Generator;
import io.github.aj8gh.countdown.solver.Solver;
import io.github.aj8gh.countdown.util.calculator.Calculator;
import io.github.aj8gh.countdown.util.serialisation.RpnConverter;
import io.github.aj8gh.countdown.util.serialisation.RpnParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode;
import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.INTERMEDIATE;
import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.SEQUENTIAL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IntegrationTest {
    private static final int NUMBER_OF_RUNS = 100;
    private static final int WARM_UPS = 5;
    private static final int MIN_TARGET = 100;
    private static final int MAX_TARGET = 999;
    private Solver solver;
    private Generator generator;
    private RpnParser rpnParser;
    private RpnConverter rpnConverter;

    @BeforeEach
    void setUp() {
        solver = new Solver();
        generator = new Generator();
        rpnParser = new RpnParser();
        rpnConverter = new RpnConverter();
    }

    @ParameterizedTest
    @MethodSource(value = "getModes")
    void sequentialSolver_sequentialGenerator(List<CalculationMode> modes) {
        generator.setMode(modes.get(0));
        solver.setMode(modes.get(1));

        for (int i = 0; i < NUMBER_OF_RUNS; i++) {
            // Test Generator
            generator.warmUp(WARM_UPS);
            generator.generate(i % 5);
            var target = generator.getTarget().getValue();
            assertTrue(target >= MIN_TARGET && target <= MAX_TARGET);
            assertEquals(rpnConverter.convert(generator.getTarget().getSolution()), generator.getTarget().getRpn());
            assertEquals(target, rpnParser.parse(generator.getTarget().getRpn()));

            // Test solver
            solver.warmUp(WARM_UPS);
            solver.solve(generator.getQuestionNumbers());
            var result = solver.getSolution().getValue();
            assertEquals(target, result);
            assertEquals(rpnConverter.convert(solver.getSolution().getSolution()), solver.getSolution().getRpn());
            assertEquals(result, rpnParser.parse(solver.getSolution().getRpn()));

            // Tear down
            generator.reset();
            solver.reset();
        }
    }

    @Test
    void switchingModes() {
        for (int i = 0; i < NUMBER_OF_RUNS; i++) {
            // Test Generator
            generator.warmUp(WARM_UPS);
            generator.generate(i % 5);
            var target = generator.getTarget().getValue();
            assertTrue(target >= MIN_TARGET && target <= MAX_TARGET);
            assertEquals(rpnConverter.convert(generator.getTarget().getSolution()), generator.getTarget().getRpn());
            assertEquals(target, rpnParser.parse(generator.getTarget().getRpn()));

            // Test solvers
            solver.warmUp(WARM_UPS);
            solver.solve(generator.getQuestionNumbers());
            var result = solver.getSolution().getValue();
            assertEquals(target, result);
            assertEquals(rpnConverter.convert(solver.getSolution().getSolution()), solver.getSolution().getRpn());
            assertEquals(result, rpnParser.parse(solver.getSolution().getRpn()));

            // Tear down
            generator.reset();
            solver.reset();

            // Do the switch
            switchModes(i);
        }
    }

    private void switchModes(int index) {
        if (index % 5 == 0) {
            generator.setMode(generator.getMode().equals(SEQUENTIAL) ? INTERMEDIATE : SEQUENTIAL);
        }
        if (index % 7 == 0) {
            solver.setMode(solver.getMode().equals(SEQUENTIAL) ? INTERMEDIATE : SEQUENTIAL);
        }
    }

    private static List<List<Calculator.CalculationMode>> getModes() {
        return List.of(
                List.of(SEQUENTIAL, SEQUENTIAL),
                List.of(INTERMEDIATE, INTERMEDIATE),
                List.of(SEQUENTIAL, INTERMEDIATE),
                List.of(INTERMEDIATE, SEQUENTIAL)
        );
    }
}
