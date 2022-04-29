package io.github.aj8gh.countdown.config;

import io.github.aj8gh.countdown.generator.FilterFactory;
import io.github.aj8gh.countdown.generator.Generator;
import io.github.aj8gh.countdown.slack.SlackClient;
import io.github.aj8gh.countdown.slack.TokenSupplier;
import io.github.aj8gh.countdown.solver.Solver;
import io.github.aj8gh.countdown.util.calculator.Calculator;
import io.github.aj8gh.countdown.util.serialisation.Deserializer;
import io.github.aj8gh.countdown.util.serialisation.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public class AppConfig {
    private static final Logger LOG = LoggerFactory.getLogger(AppConfig.class);
    private static final String PROPS_FILE_PATH = "config/src/main/resources/application.properties";
    private static final Properties PROPS = new Properties();

    private static Generator generator;
    private static Solver solver;
    private static SlackClient slackClient;
    private static Deserializer deserializer;
    private static Serializer serializer;

    static {
        loadProperties();
        buildGenerator();
        buildSolver();
        buildSlackClient();
        buildDeserializer();
        buildSerializer();
    }

    public static Generator generator() {
        return generator;
    }

    public static Solver solver() {
        return solver;
    }

    public static SlackClient slackClient() {
        return slackClient;
    }

    public static Deserializer deserializer() {
        return deserializer;
    }

    public static Serializer serializer() {
        return serializer;
    }

    private static void loadProperties() {
        try (FileInputStream in = new FileInputStream(PROPS_FILE_PATH)) {
            PROPS.load(in);
        } catch (IOException e) {
            LOG.error("Error loading app properties\n", e);
        }
    }

    private static void buildGenerator() {
        generator = new Generator();
        var mode = Calculator.CalculationMode.valueOf(PROPS.getProperty("generator.mode"));
        var warmUps = Integer.parseInt(PROPS.getProperty("generator.warmups"));
        var filters = PROPS.getProperty("generator.filters").split(",");
        generator.setMode(mode);
        generator.setWarmUps(warmUps);
        Arrays.stream(filters)
                .map(FilterFactory.Filter::valueOf)
                .forEach(f -> generator.addFilter(f.getPredicate()));
    }

    private static void buildSolver() {
        solver = new Solver();
        var mode = Calculator.CalculationMode.valueOf(PROPS.getProperty("solver.mode"));
        var warmUps = Integer.parseInt(PROPS.getProperty("solver.warmups"));
        var switchThreshold = Integer.parseInt(PROPS.getProperty("solver.switch.threshold"));
        solver.setMode(mode);
        solver.setWarmUps(warmUps);
        solver.setModeSwitchThreshold(switchThreshold);
    }

    private static void buildSlackClient() {
        var slackPropsPath = PROPS.getProperty("slack.propsFilePath");
        var slackTokenKey = PROPS.getProperty("slack.tokenKey");
        var slackChannel = PROPS.getProperty("slack.channel");
        var tokenSupplier = new TokenSupplier(slackPropsPath, slackTokenKey);
        slackClient = new SlackClient(tokenSupplier);
        slackClient.setChannel(slackChannel);
    }

    private static void buildDeserializer() {
        var ioDir = PROPS.getProperty("inputOutput.dir");
        var solInFile = PROPS.getProperty("solver.input.file");
        var genInFile = PROPS.getProperty("generator.input.file");
        deserializer = new Deserializer(ioDir, solInFile, genInFile);
    }

    private static void buildSerializer() {
        var ioDir = PROPS.getProperty("inputOutput.dir");
        var genOutFile = PROPS.getProperty("generator.output.file");
        var solOutFile = PROPS.getProperty("solver.output.file");
        var solInFile = PROPS.getProperty("solver.input.file");
        serializer = new Serializer(ioDir, genOutFile, solOutFile, solInFile);
    }
}
