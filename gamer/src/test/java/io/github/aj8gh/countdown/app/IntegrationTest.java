package io.github.aj8gh.countdown.app;

import io.github.aj8gh.countdown.calc.Calculator;
import io.github.aj8gh.countdown.util.RpnConverter;
import io.github.aj8gh.countdown.util.RpnParser;
import io.github.aj8gh.countdown.conf.AppConfig;
import io.github.aj8gh.countdown.gen.Generator;
import io.github.aj8gh.countdown.sol.Solver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static io.github.aj8gh.countdown.calc.Calculator.CalculationMode;
import static io.github.aj8gh.countdown.calc.Calculator.CalculationMode.INTERMEDIATE;
import static io.github.aj8gh.countdown.calc.Calculator.CalculationMode.RECURSIVE;
import static io.github.aj8gh.countdown.calc.Calculator.CalculationMode.SEQUENTIAL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IntegrationTest {
    private static final int NUMBER_OF_RUNS = 20;
    private static final int WARM_UPS = 10;
    private static final int MIN_TARGET = 100;
    private static final int MAX_TARGET = 999;

    private Solver solver;
    private Generator generator;
    private RpnParser rpnParser;
    private RpnConverter rpnConverter;

    @BeforeEach
    void setUp() {
        var config = new AppConfig();
        solver = config.solver();
        solver.setSwitchModes(true);
        generator = config.generator();
        rpnParser = new RpnParser();
        rpnConverter = new RpnConverter();

        solver.setWarmUps(WARM_UPS);
        generator.setWarmUps(WARM_UPS);
    }

    @ParameterizedTest
    @MethodSource(value = "getModes")
    void solverAndGenerator_FixedMode(List<CalculationMode> modes) {
        for (int i = 0; i < NUMBER_OF_RUNS; i++) {
            generator.setMode(modes.get(0));
            solver.setMode(modes.get(1));
            testGenerator(i);
            testSolver(generator.getTarget().getValue());
            tearDown();
        }
    }

    @Test
    void solverAndGenerator_SwitchingModes() {
        for (int i = 0; i < NUMBER_OF_RUNS * 2; i++) {
            testGenerator(i);
            testSolver(generator.getTarget().getValue());
            tearDown();
            switchModes(i);
        }
    }

    private void testGenerator(int i) {
        generator.warmUp();
        generator.generate(i % 5);
        var target = generator.getTarget().getValue();
        assertTrue(target >= MIN_TARGET && target <= MAX_TARGET);
        var rpn = rpnConverter.convert(generator.getTarget().toString());
        assertEquals(target, rpnParser.parse(rpn));
    }

    private void testSolver(int target) {
        solver.warmUp();
        solver.solve(generator.getQuestionNumbers());
        var result = solver.getSolution().getValue();
        assertEquals(target, result);
        var rpn = rpnConverter.convert(solver.getSolution().toString());
        assertEquals(result, rpnParser.parse(rpn));
    }

    private void tearDown() {
        generator.reset();
        solver.reset();
    }

    private void switchModes(int index) {
        if (index % 2 == 0) {
            generator.setMode(SEQUENTIAL);
        } else if (index % 3 == 0) {
            generator.setMode(INTERMEDIATE);
        } else if (index % 5 == 0) {
            solver.setMode(RECURSIVE);
        }
    }

    private static List<List<Calculator.CalculationMode>> getModes() {
        return List.of(
                List.of(SEQUENTIAL, SEQUENTIAL),
                List.of(INTERMEDIATE, INTERMEDIATE),
                List.of(SEQUENTIAL, INTERMEDIATE),
                List.of(INTERMEDIATE, SEQUENTIAL),
                List.of(SEQUENTIAL, RECURSIVE),
                List.of(INTERMEDIATE, RECURSIVE)
        );
    }
}
