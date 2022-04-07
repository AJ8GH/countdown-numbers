package io.github.aj8gh.countdown.generator;

import io.github.aj8gh.countdown.util.calculator.Calculator;
import io.github.aj8gh.countdown.util.serialisation.Deserializer;
import io.github.aj8gh.countdown.util.serialisation.Serializer;
import io.github.aj8gh.countdown.util.timer.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;

import static io.github.aj8gh.countdown.util.calculator.CalculationMode.SEQUENTIAL;
import static io.github.aj8gh.countdown.util.calculator.CalculationMode;


public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private static final int WARM_UPS = 50;
    private static final CalculationMode DEFAULT_MODE = SEQUENTIAL;

    private static final Deserializer DESERIALIZER = new Deserializer();
    private static final Serializer SERIALIZER = new Serializer();
    private static final Generator GENERATOR = new Generator(new Calculator(), new Timer(), WARM_UPS);

    static {
        GENERATOR.setMode(DEFAULT_MODE);
    }

    public static void main(String... args) {
        var numLarge = readFromFile(args);
        warmUp();
        GENERATOR.generate(numLarge);
        logGenerator();
        writeToFile();
    }

    private static int readFromFile(String... args) {
        LOG.info("*** Reading gen.in ***");
        var file = args.length == 0 ? null : args[0];
        return DESERIALIZER.forGenerator(file);
    }

    private static void writeToFile() {
        LOG.info("*** Creating gen.out ***");
        SERIALIZER.serializeGenerator(GENERATOR.getTarget().getRpn(),
                GENERATOR.getTarget().getValue(), GENERATOR.getTime());

        LOG.info("*** Creating sol.in");
        SERIALIZER.createSolverInput(GENERATOR.getQuestionNumbers());
    }

    private static void warmUp() {
        for (int i = 0; i < WARM_UPS; i++) {
            GENERATOR.generate(ThreadLocalRandom.current().nextInt(5));
            GENERATOR.reset();
        }
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
