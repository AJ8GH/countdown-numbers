package io.github.aj8gh.countdown.app.conf;

import io.github.aj8gh.countdown.app.cli.CliInputSupplier;
import io.github.aj8gh.countdown.app.cli.CountdownApp;
import io.github.aj8gh.countdown.app.game.Game;
import io.github.aj8gh.countdown.calc.Calculator;
import io.github.aj8gh.countdown.gen.FilterFactory;
import io.github.aj8gh.countdown.gen.Generator;
import io.github.aj8gh.countdown.in.FileInputSupplier;
import io.github.aj8gh.countdown.in.InputSupplier;
import io.github.aj8gh.countdown.in.PropsInputSupplier;
import io.github.aj8gh.countdown.out.OutputHandler;
import io.github.aj8gh.countdown.out.OutputRouter;
import io.github.aj8gh.countdown.out.file.Deserializer;
import io.github.aj8gh.countdown.out.file.FileHandler;
import io.github.aj8gh.countdown.out.file.Serializer;
import io.github.aj8gh.countdown.out.slack.SlackClient;
import io.github.aj8gh.countdown.out.slack.SlackHandler;
import io.github.aj8gh.countdown.sol.Solver;

import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;

import static io.github.aj8gh.countdown.calc.Calculator.CalculationMode;
import static io.github.aj8gh.countdown.out.OutputHandler.OutputType.FILE;
import static io.github.aj8gh.countdown.out.OutputHandler.OutputType.SLACK;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

public class AppConfig {
    private static final Generator GENERATOR;
    private static final Solver SOLVER;
    private static final Deserializer DESERIALIZER;
    private static final Properties PROPS;
    private static final OutputHandler OUTPUT_HANDLER;

    static {
        try {
            PROPS = new PropsConfig().getProps();
            GENERATOR = buildGenerator();
            SOLVER = buildSolver();
            DESERIALIZER = buildDeserializer();
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

    public static OutputHandler outputHandler() {
        return OUTPUT_HANDLER;
    }

    public static Consumer<String[]> app() {
        var runner = PROPS.getProperty("app.runner");
        return runner.equals("cli") ?
                new CountdownApp(outputHandler(), new CliInputSupplier(), generator(), solver()) :
                new Game(outputHandler(), buildInputSupplier(), generator(), solver());
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
        var solver = new Solver(generator, calculators);
        solver.setMode(CalculationMode.valueOf(PROPS.getProperty("solver.mode")));
        solver.setTimeScale(Integer.parseInt(PROPS.getProperty("solver.timer.scale")));
        solver.setCaching(Boolean.parseBoolean(PROPS.getProperty("solver.caching")));
        solver.setWarmUps(Integer.parseInt(PROPS.getProperty("solver.warmups")));
        solver.setSwitchModes(Boolean.parseBoolean(PROPS.getProperty("solver.switch.modes")));
        solver.setModeSwitchThreshold(Integer.parseInt(PROPS.getProperty("solver.switch.threshold")));
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

    private static FileHandler buildFileHandler() {
        var createSolverInput = Boolean.parseBoolean(PROPS.getProperty("output.create.solver.input"));
        var serializer = new Serializer();
        var fileHandler = new FileHandler(serializer);
        fileHandler.setIoDir(PROPS.getProperty("inputOutput.dir"));
        fileHandler.setGenOutFile(PROPS.getProperty("output.generator.file"));
        fileHandler.setSolOutFile(PROPS.getProperty("output.solver.file"));
        fileHandler.setSolInFile(PROPS.getProperty("input.solver.file"));
        fileHandler.setCreateSolverInput(createSolverInput);
        return fileHandler;
    }

    private static InputSupplier buildInputSupplier() {
        return PROPS.getProperty("input.type").equalsIgnoreCase("FILE") ?
                new FileInputSupplier(buildDeserializer()) :
                new PropsInputSupplier(Arrays.stream(PROPS.getProperty("input.solver").split(","))
                        .map(n -> Integer.valueOf(n.trim()))
                        .toList(),
                        Integer.parseInt(PROPS.getProperty("input.generator")));
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
                        if (type.equals(FILE)) return buildFileHandler();
                        if (type.equals(SLACK)) return buildSlackHandler();
                        var handlerType = Class.forName(type.handlerType().getName());
                        return (OutputHandler) handlerType.getConstructor().newInstance();
                    } catch (Exception e) {
                        throw new IllegalStateException("Error building handlers", e);
                    }
                }));
    }
}
