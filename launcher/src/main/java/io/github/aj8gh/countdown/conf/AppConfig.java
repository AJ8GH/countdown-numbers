package io.github.aj8gh.countdown.conf;

import io.github.aj8gh.countdown.calc.CalculatorManager;
import io.github.aj8gh.countdown.cli.CliInputSupplier;
import io.github.aj8gh.countdown.cli.CliApp;
import io.github.aj8gh.countdown.game.GameApp;
import io.github.aj8gh.countdown.calc.Calculator;
import io.github.aj8gh.countdown.gen.FilterFactory;
import io.github.aj8gh.countdown.gen.Generator;
import io.github.aj8gh.countdown.in.file.FileInputSupplier;
import io.github.aj8gh.countdown.in.InputSupplier;
import io.github.aj8gh.countdown.in.props.PropsInputSupplier;
import io.github.aj8gh.countdown.in.rand.RandomInputSupplier;
import io.github.aj8gh.countdown.out.OutputHandler;
import io.github.aj8gh.countdown.out.OutputRouter;
import io.github.aj8gh.countdown.in.file.Deserializer;
import io.github.aj8gh.countdown.out.file.FileHandler;
import io.github.aj8gh.countdown.out.file.Serializer;
import io.github.aj8gh.countdown.out.slack.SlackClient;
import io.github.aj8gh.countdown.out.slack.SlackHandler;
import io.github.aj8gh.countdown.sol.Solver;
import io.github.aj8gh.countdown.test.TestApp;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static io.github.aj8gh.countdown.calc.Calculator.CalculationMode;
import static io.github.aj8gh.countdown.out.OutputHandler.OutputType.FILE;
import static io.github.aj8gh.countdown.out.OutputHandler.OutputType.SLACK;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

public class AppConfig {
    private static final PropsConfig PROPS = new PropsConfig();

    public Consumer<String[]> app() {
        if (PROPS.getString("app.runner").equals("cli")) {
            return new CliApp(outputHandler(), new CliInputSupplier(), generator(), solver());
        } else if (PROPS.getString("app.runner").equals("test")) {
            return new TestApp(inputSupplier());
        }
        return new GameApp(outputHandler(), inputSupplier(), generator(), solver());
    }

    public Generator generator() {
        var calculators = getCalculators();
        var generator = new Generator(calculators);
        generator.setMode(Calculator.CalculationMode.valueOf(PROPS.getString("generator.mode")));
        generator.setWarmUps(PROPS.getInt("generator.warmups"));
        generator.setTimeScale(PROPS.getInt("generator.timer.scale"));
        PROPS.getStrings("generator.filters").stream()
                .map(FilterFactory.Filter::valueOf)
                .forEach(f -> generator.addFilter(f.getPredicate()));
        return generator;
    }

    public Solver solver() {
        var solver = new Solver(generator(), calculatorManager());
        solver.setMode(CalculationMode.valueOf(PROPS.getString("solver.mode")));
        solver.setTimeScale(PROPS.getInt("solver.timer.scale"));
        solver.setCaching(PROPS.getBoolean("solver.caching"));
        solver.setWarmUps(PROPS.getInt("solver.warmups"));
        solver.setMaxNumbers(PROPS.getInt("solver.maxNumbers"));
        solver.setMaxNumberThreshold(PROPS.getInt("solver.maxNumber.threshold"));
        return solver;
    }

    public OutputHandler outputHandler() {
        var activeHandlers = PROPS.getStrings("output.types").stream()
                .map(OutputHandler.OutputType::valueOf)
                .collect(toSet());
        var outputHandlers = getOutputHandlers();
        return new OutputRouter(activeHandlers, outputHandlers);
    }

    public InputSupplier inputSupplier() {
        if (PROPS.getString("input.type").equalsIgnoreCase("FILE")) {
            return new FileInputSupplier(deserializer());
        } else if (PROPS.getString("input.type").equalsIgnoreCase("PROPS")) {
            return new PropsInputSupplier(
                    PROPS.getInts("input.solver"),
                    PROPS.getInt("input.generator"));
        }
        return new RandomInputSupplier(generator());
    }

    private SlackHandler slackHandler() {
        var slackClient = new SlackClient(PROPS.getString("slack.oauth.token"));
        var slackHandler = new SlackHandler(slackClient);
        slackHandler.setChannel(PROPS.getString("output.slack.channel"));
        return slackHandler;
    }

    private Deserializer deserializer() {
        var ioDir = PROPS.getString("inputOutput.dir");
        var solInFile = PROPS.getString("input.solver.file");
        var genInFile = PROPS.getString("input.generator.file");
        return new Deserializer(ioDir, solInFile, genInFile);
    }

    private FileHandler fileHandler() {
        var fileHandler = new FileHandler(new Serializer());
        fileHandler.setIoDir(PROPS.getString("inputOutput.dir"));
        fileHandler.setGenOutFile(PROPS.getString("output.generator.file"));
        fileHandler.setSolOutFile(PROPS.getString("output.solver.file"));
        fileHandler.setSolInFile(PROPS.getString("input.solver.file"));
        fileHandler.setCreateSolverInput(PROPS.getBoolean("output.create.solver.input"));
        return fileHandler;
    }

    private CalculatorManager calculatorManager() {
        var calculatorManager = new CalculatorManager(getCalculators());
        calculatorManager.setSwitchModes(PROPS.getBoolean("solver.switch.modes"));
        calculatorManager.setIntermediateThreshold(PROPS.getLong("solver.intermediate.threshold"));
        calculatorManager.setSequentialThreshold(PROPS.getLong("solver.sequential.threshold"));
        calculatorManager.setRecursiveThreshold(PROPS.getLong("solver.recursive.threshold"));
        return calculatorManager;
    }

    private Map<Calculator.CalculationMode, Calculator> getCalculators() {
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

    private Map<OutputHandler.OutputType, OutputHandler> getOutputHandlers() {
        return Arrays.stream(OutputHandler.OutputType.values())
                .collect(toMap(Function.identity(), type -> {
                    try {
                        if (type.equals(FILE)) return fileHandler();
                        if (type.equals(SLACK)) return slackHandler();
                        var handlerType = Class.forName(type.handlerType().getName());
                        return (OutputHandler) handlerType.getConstructor().newInstance();
                    } catch (Exception e) {
                        throw new IllegalStateException("Error building handlers", e);
                    }
                }));
    }
}
