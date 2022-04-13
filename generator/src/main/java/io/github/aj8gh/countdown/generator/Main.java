package io.github.aj8gh.countdown.generator;

import io.github.aj8gh.countdown.util.calculator.Calculator;
import io.github.aj8gh.countdown.util.serialisation.Deserializer;
import io.github.aj8gh.countdown.util.serialisation.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.INTERMEDIATE;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private static final Calculator.CalculationMode DEFAULT_MODE = INTERMEDIATE;
    private static final Deserializer DESERIALIZER = new Deserializer();
    private static final Serializer SERIALIZER = new Serializer();
    private static final Generator GENERATOR = new Generator();
    private static final int WARM_UPS = 25;

    public static void main(String... args) {
        setUp();
        var numLarge = readFile(args);
        GENERATOR.warmUp(WARM_UPS);
        GENERATOR.generate(numLarge);
        logGenerator();
        writeFile();
    }

    private static int readFile(String... args) {
        var file = args.length == 0 ? null : args[0];
        return DESERIALIZER.forGenerator(file);
    }

    private static void writeFile() {
        SERIALIZER.serializeGenerator(GENERATOR.getTarget().getRpn(),
                GENERATOR.getTarget().getValue(), GENERATOR.getTime());
        SERIALIZER.createSolverInput(GENERATOR.getQuestionNumbers());
    }

    private static void setUp() {
        GENERATOR.setMode(DEFAULT_MODE);
    }

    private static void logGenerator() {
        LOG.info("""
                        
                        ============================================================================
                        GENERATOR
                        Question:     {}
                        Method:       {} = {}
                        RPN:          {}
                        Attempts:     {}
                        Time:         {} ms
                        Mode:         {}
                        ============================================================================
                        """,
                GENERATOR.getQuestionNumbers(),
                GENERATOR.getTarget().getSolution(),
                GENERATOR.getTarget().getValue(),
                GENERATOR.getTarget().getRpn(),
                GENERATOR.getAttempts(),
                GENERATOR.getTime(),
                GENERATOR.getMode()
        );
    }
}
