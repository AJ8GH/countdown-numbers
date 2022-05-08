package io.github.aj8gh.countdown.conf;

import io.github.aj8gh.countdown.BaseApp;
import io.github.aj8gh.countdown.calc.CalculatorManager;
import io.github.aj8gh.countdown.cli.CliInputSupplier;
import io.github.aj8gh.countdown.cli.CliApp;
import io.github.aj8gh.countdown.game.GameApp;
import io.github.aj8gh.countdown.calc.Calculator;
import io.github.aj8gh.countdown.gen.DifficultyAnalyser;
import io.github.aj8gh.countdown.gen.Filter;
import io.github.aj8gh.countdown.gen.GenAdaptor;
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
import io.github.aj8gh.countdown.sol.SolAdaptor;
import io.github.aj8gh.countdown.sol.SolutionCache;
import io.github.aj8gh.countdown.sol.Solver;
import io.github.aj8gh.countdown.test.TestApp;
import io.github.aj8gh.countdown.util.Timer;

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
        var baseApp = new BaseApp(genAdaptor(), solAdaptor(), outputHandler());
        if (PROPS.getString("app.runner").equals("cli")) {
            return new CliApp(baseApp, new CliInputSupplier());
        } else if (PROPS.getString("app.runner").equals("test")) {
            return new TestApp(baseApp, inputSupplier());
        }
        return new GameApp(baseApp, inputSupplier());
    }

    public Generator generator() {
        var calculatorManager = calculatorManager();
        calculatorManager.setSwitchModes(PROPS.getBoolean("generator.switch.modes"));
        calculatorManager.setIntermediateThreshold(PROPS.getLong("generator.intermediate.threshold"));
        calculatorManager.setSequentialThreshold(PROPS.getLong("generator.sequential.threshold"));
        calculatorManager.setRecursiveThreshold(PROPS.getLong("generator.recursive.threshold"));

        var generator = new Generator(calculatorManager);
        generator.setMode(Calculator.CalculationMode.valueOf(PROPS.getString("generator.mode")));
        generator.setWarmUps(PROPS.getInt("generator.warmups"));
        PROPS.getStrings("generator.filters").stream()
                .map(Filter.FilterType::valueOf)
                .forEach(f -> generator.addFilter(f.getPredicate()));
        return generator;
    }

    public SolAdaptor solAdaptor() {
        var timer = new Timer();
        timer.setTimescale(PROPS.getInt("solver.timer.scale"));
        var solAdaptor = new SolAdaptor(new SolutionCache(), timer, solver());
        solAdaptor.setCaching(PROPS.getBoolean("solver.caching"));
        solAdaptor.setMaxNumbers(PROPS.getInt("solver.maxNumbers"));
        solAdaptor.setMaxNumberThreshold(PROPS.getInt("solver.maxNumber.threshold"));
        solAdaptor.setCheckDifficulty(PROPS.getBoolean("solver.difficulty.check"));
        return solAdaptor;
    }

    public Solver solver() {
        var calculatorManager = calculatorManager();
        calculatorManager.setSwitchModes(PROPS.getBoolean("solver.switch.modes"));
        calculatorManager.setIntermediateThreshold(PROPS.getLong("solver.intermediate.threshold"));
        calculatorManager.setSequentialThreshold(PROPS.getLong("solver.sequential.threshold"));
        calculatorManager.setRecursiveThreshold(PROPS.getLong("solver.recursive.threshold"));

        var solver = new Solver(generator(), calculatorManager);
        solver.setMode(CalculationMode.valueOf(PROPS.getString("solver.mode")));
        solver.setWarmUps(PROPS.getInt("solver.warmups"));
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

    public GenAdaptor genAdaptor() {
        var difficultyAnalyser = new DifficultyAnalyser(solver());
        difficultyAnalyser.setRuns(PROPS.getInt("generator.difficulty.runs"));
        difficultyAnalyser.setMinDifficulty(PROPS.getDouble("generator.difficulty.min"));
        var timer = new Timer();
        timer.setTimescale(PROPS.getInt("generator.timer.scale"));
        var genAdaptor = new GenAdaptor(generator(), difficultyAnalyser, timer);
        genAdaptor.setCheckDifficulty(PROPS.getBoolean("generator.difficulty.check"));
        return genAdaptor;
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
        return new CalculatorManager(getCalculators());
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
