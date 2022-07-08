package io.github.aj8gh.countdown.conf;

import static io.github.aj8gh.countdown.calc.Calculator.CalculationMode;

import io.github.aj8gh.countdown.calc.Calculator;
import io.github.aj8gh.countdown.calc.CalculatorManager;
import io.github.aj8gh.countdown.calc.impl.IntermediateCalculator;
import io.github.aj8gh.countdown.calc.impl.RecursiveCalculator;
import io.github.aj8gh.countdown.calc.impl.SequentialCalculator;
import io.github.aj8gh.countdown.game.Gamer;
import io.github.aj8gh.countdown.game.GamerBuilder;
import io.github.aj8gh.countdown.game.Timer;
import io.github.aj8gh.countdown.gen.Filter;
import io.github.aj8gh.countdown.gen.Generator;
import io.github.aj8gh.countdown.ser.Deserializer;
import io.github.aj8gh.countdown.ser.Serializer;
import io.github.aj8gh.countdown.sol.SolutionCache;
import io.github.aj8gh.countdown.sol.Solver;
import java.util.Map;

public class AppConfig {
    private static final PropsConfig PROPS = new PropsConfig();

    public Gamer gamer() {
        return new Gamer(GamerBuilder.builder()
                .deserializer(deserializer())
                .serializer(serializer())
                .generator(generator())
                .solver(solver())
                .timer(timer())
                .build());
    }

    public Generator generator() {
        var calculatorManager = calculatorManager();
        calculatorManager.setSwitchModes(PROPS.getBoolean("generator.switch.modes"));
        calculatorManager.setIntermediateThreshold(PROPS.getLong("generator.intermediate.threshold"));
        calculatorManager.setSequentialThreshold(PROPS.getLong("generator.sequential.threshold"));

        var generator = new Generator(calculatorManager);
        generator.setMode(Calculator.CalculationMode.valueOf(PROPS.getString("generator.mode")));
        generator.setWarmups(PROPS.getInt("generator.warmups"));
        PROPS.getStrings("generator.filters").stream()
                .map(Filter.FilterType::valueOf)
                .forEach(f -> generator.addFilter(f.getPredicate()));
        return generator;
    }

    public Solver solver() {
        var calculatorManager = calculatorManager();
        calculatorManager.setSwitchModes(PROPS.getBoolean("solver.switch.modes"));
        calculatorManager.setIntermediateThreshold(PROPS.getLong("solver.intermediate.threshold"));
        calculatorManager.setSequentialThreshold(PROPS.getLong("solver.sequential.threshold"));

        var solver = new Solver(generator(), calculatorManager, solutionCache());
        solver.setMode(CalculationMode.valueOf(PROPS.getString("solver.mode")));
        solver.setOptimiseNumbersThreshold(PROPS.getInt("solver.optimiseNumbers.threshold"));
        solver.setOptimiseNumbers(PROPS.getBoolean("solver.optimiseNumbers"));
        solver.setCaching(PROPS.getBoolean("solver.caching"));
        solver.setWarmups(PROPS.getInt("solver.warmups"));
        return solver;
    }

    private SolutionCache solutionCache() {
        return new SolutionCache();
    }

    private Serializer serializer() {
        return new Serializer();
    }

    private Deserializer deserializer() {
        return new Deserializer();
    }

    private Timer timer() {
        var timeUnit = Timer.Unit.valueOf(PROPS.getString("timer.unit"));
        return new Timer(timeUnit);
    }

    private CalculatorManager calculatorManager() {
        return new CalculatorManager(getCalculators());
    }

    private Map<Calculator.CalculationMode, Calculator> getCalculators() {
        return Map.of(
                CalculationMode.SEQUENTIAL, new SequentialCalculator(),
                CalculationMode.INTERMEDIATE, new IntermediateCalculator(),
                CalculationMode.RECURSIVE, new RecursiveCalculator()
        );
    }
}
