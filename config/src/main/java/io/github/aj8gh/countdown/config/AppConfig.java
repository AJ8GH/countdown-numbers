package io.github.aj8gh.countdown.config;

import io.github.aj8gh.countdown.generator.FilterFactory;
import io.github.aj8gh.countdown.generator.Generator;
import io.github.aj8gh.countdown.slack.SlackClient;
import io.github.aj8gh.countdown.solver.Solver;
import io.github.aj8gh.countdown.util.calculator.Calculator;
import io.github.aj8gh.countdown.util.serialisation.Deserializer;
import io.github.aj8gh.countdown.util.serialisation.Serializer;

import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

public class AppConfig {
    private static final Generator GENERATOR;
    private static final Solver SOLVER;
    private static final SlackClient SLACK_CLIENT;
    private static final Deserializer DESERIALIZER;
    private static final Serializer SERIALIZER;
    private static final Properties PROPS;

    static {
        try {
            PROPS = new PropsConfig().getProps();
            GENERATOR = buildGenerator();
            SOLVER = buildSolver();
            SLACK_CLIENT = buildSlackClient();
            DESERIALIZER = buildDeserializer();
            SERIALIZER = buildSerializer();
        } catch (Exception e) {
            throw new IllegalStateException("Illegal Application Context", e);
        }
    }

    public static Generator generator() {
        return GENERATOR;
    }

    public static Solver solver() {
        return SOLVER;
    }

    public static SlackClient slackClient() {
        return SLACK_CLIENT;
    }

    public static Deserializer deserializer() {
        return DESERIALIZER;
    }

    public static Serializer serializer() {
        return SERIALIZER;
    }

    private static Generator buildGenerator() {
        var calculators = getCalculators();
        var mode = Calculator.CalculationMode.valueOf(PROPS.getProperty("generator.mode"));
        var warmUps = Integer.parseInt(PROPS.getProperty("generator.warmups"));
        var filters = PROPS.getProperty("generator.filters").split(",");
        var timeScale = Integer.parseInt(PROPS.getProperty("generator.timer.scale"));
        var generator = new Generator(calculators);
        generator.setMode(mode);
        generator.setWarmUps(warmUps);
        generator.setTimeScale(timeScale);
        Arrays.stream(filters)
                .map(FilterFactory.Filter::valueOf)
                .forEach(f -> generator.addFilter(f.getPredicate()));

        return generator;
    }

    private static Solver buildSolver() {
        var calculators = getCalculators();
        var generator = buildGenerator();
        var mode = Calculator.CalculationMode.valueOf(PROPS.getProperty("solver.mode"));
        var warmUps = Integer.parseInt(PROPS.getProperty("solver.warmups"));
        var switchThreshold = Integer.parseInt(PROPS.getProperty("solver.switch.threshold"));
        var timeScale = Integer.parseInt(PROPS.getProperty("solver.timer.scale"));
        var solver = new Solver(generator, calculators);
        solver.setMode(mode);
        solver.setTimeScale(timeScale);
        solver.setWarmUps(warmUps);
        solver.setModeSwitchThreshold(switchThreshold);
        return solver;
    }

    private static SlackClient buildSlackClient() {
        var slackToken = PROPS.getProperty("slack.oauth.token");
        var slackClient = new SlackClient(slackToken);
        slackClient.setChannel(PROPS.getProperty("slack.channel"));
        return slackClient;
    }

    private static Deserializer buildDeserializer() {
        var ioDir = PROPS.getProperty("inputOutput.dir");
        var solInFile = PROPS.getProperty("solver.input.file");
        var genInFile = PROPS.getProperty("generator.input.file");
        return new Deserializer(ioDir, solInFile, genInFile);
    }

    private static Serializer buildSerializer() {
        var ioDir = PROPS.getProperty("inputOutput.dir");
        var genOutFile = PROPS.getProperty("generator.output.file");
        var solOutFile = PROPS.getProperty("solver.output.file");
        var solInFile = PROPS.getProperty("solver.input.file");
        return new Serializer(ioDir, genOutFile, solOutFile, solInFile);
    }

    private static Map<Calculator.CalculationMode, Calculator> getCalculators() {
        return Arrays.stream(Calculator.CalculationMode.values()).collect(toMap(Function.identity(), mode -> {
            try {
                var type = Class.forName(mode.type().getName());
                return (Calculator) type.getConstructor().newInstance();
            } catch (Exception e) {
                throw new IllegalStateException("Error building Calculator map", e);
            }
        }));
    }
}
