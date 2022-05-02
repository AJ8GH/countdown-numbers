package io.github.aj8gh.countdown.conf;

import io.github.aj8gh.countdown.calc.Calculator;
import io.github.aj8gh.countdown.gen.FilterFactory;
import io.github.aj8gh.countdown.gen.Generator;
import io.github.aj8gh.countdown.out.OutputHandler;
import io.github.aj8gh.countdown.out.OutputRouter;
import io.github.aj8gh.countdown.out.serial.Deserializer;
import io.github.aj8gh.countdown.out.serial.Serializer;
import io.github.aj8gh.countdown.out.slack.SlackClient;
import io.github.aj8gh.countdown.out.slack.SlackHandler;
import io.github.aj8gh.countdown.sol.Solver;
import io.github.aj8gh.countdown.util.test.InputProvider;

import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

import static io.github.aj8gh.countdown.out.OutputHandler.OutputType.FILE;
import static io.github.aj8gh.countdown.out.OutputHandler.OutputType.SLACK;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

public class AppConfig {
    private static final Generator GENERATOR;
    private static final Solver SOLVER;
    private static final SlackHandler SLACK_HANDLER;
    private static final Deserializer DESERIALIZER;
    private static final Serializer SERIALIZER;
    private static final Properties PROPS;
    private static final InputProvider INPUT_PROVIDER;
    private static final OutputHandler OUTPUT_HANDLER;

    static {
        try {
            PROPS = new PropsConfig().getProps();
            GENERATOR = buildGenerator();
            SOLVER = buildSolver();
            SLACK_HANDLER = buildSlackHandler();
            DESERIALIZER = buildDeserializer();
            SERIALIZER = buildSerializer();
            INPUT_PROVIDER = buildInputProvider();
            OUTPUT_HANDLER = buildOutputHandler();
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

    public static Deserializer deserializer() {
        return DESERIALIZER;
    }

    public static InputProvider inputProvider() {
        return INPUT_PROVIDER;
    }

    public static OutputHandler outputHandler() {
        return OUTPUT_HANDLER;
    }

    private static SlackHandler slackHandler() {
        return SLACK_HANDLER;
    }

    private static Serializer serializer() {
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
        var switchModes = Boolean.parseBoolean(PROPS.getProperty("solver.switch.modes"));
        var switchThreshold = Integer.parseInt(PROPS.getProperty("solver.switch.threshold"));
        var timeScale = Integer.parseInt(PROPS.getProperty("solver.timer.scale"));
        var solver = new Solver(generator, calculators);
        solver.setMode(mode);
        solver.setTimeScale(timeScale);
        solver.setWarmUps(warmUps);
        solver.setSwitchModes(switchModes);
        solver.setModeSwitchThreshold(switchThreshold);
        return solver;
    }

    private static SlackHandler buildSlackHandler() {
        var slackToken = PROPS.getProperty("slack.oauth.token");
        var slackClient = new SlackClient(slackToken);
        var slackHandler = new SlackHandler(slackClient);
        slackHandler.setChannel(PROPS.getProperty("output.slack.channel"));
        return slackHandler;
    }

    private static Deserializer buildDeserializer() {
        var ioDir = PROPS.getProperty("inputOutput.dir");
        var solInFile = PROPS.getProperty("input.solver.file");
        var genInFile = PROPS.getProperty("input.generator.file");
        return new Deserializer(ioDir, solInFile, genInFile);
    }

    private static Serializer buildSerializer() {
        var ioDir = PROPS.getProperty("inputOutput.dir");
        var genOutFile = PROPS.getProperty("output.generator.file");
        var solOutFile = PROPS.getProperty("output.solver.file");
        var solInFile = PROPS.getProperty("input.solver.file");
        var createSolverInput = Boolean.parseBoolean(PROPS.getProperty("output.create.solver.input"));
        var serializer = new Serializer(ioDir, genOutFile, solOutFile, solInFile);
        serializer.setCreateSolverInput(createSolverInput);
        return serializer;
    }

    private static InputProvider buildInputProvider() {
        var input = Arrays.stream(PROPS.getProperty("test.input").split(","))
                .map(n -> Integer.valueOf(n.trim()))
                .toList();
        return new InputProvider(input);
    }

    private static OutputHandler buildOutputHandler() {
        var activeHandlers = Arrays.stream(PROPS.getProperty("output.types").split(","))
                .map(type -> OutputHandler.OutputType.valueOf(type.trim()))
                .collect(toSet());
        var outputHandlers = getOutputHandlers();
        return new OutputRouter(activeHandlers, outputHandlers);
    }

    private static Map<Calculator.CalculationMode, Calculator> getCalculators() {
        return Arrays.stream(Calculator.CalculationMode.values())
                .collect(toMap(Function.identity(), mode -> {
                    try {
                        var type = Class.forName(mode.type().getName());
                        return (Calculator) type.getConstructor().newInstance();
                    } catch (Exception e) {
                        throw new IllegalStateException("Error building Calculator map", e);
                    }
                }));
    }

    private static Map<OutputHandler.OutputType, OutputHandler> getOutputHandlers() {
        return Arrays.stream(OutputHandler.OutputType.values())
                .collect(toMap(Function.identity(), type -> {
                    try {
                        if (type.equals(FILE)) return serializer();
                        if (type.equals(SLACK)) return slackHandler();
                        var handlerType = Class.forName(type.handlerType().getName());
                        return (OutputHandler) handlerType.getConstructor().newInstance();
                    } catch (Exception e) {
                        throw new IllegalStateException("Error building handlers", e);
                    }
                }));
    }
}
