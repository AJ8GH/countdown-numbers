package io.github.aj8gh.countdown.solver;

import io.github.aj8gh.countdown.generator.Generator;
import io.github.aj8gh.countdown.util.calculator.impl.CalculatorV1;
import io.github.aj8gh.countdown.util.serialisation.Deserializer;
import io.github.aj8gh.countdown.util.serialisation.Serializer;
import io.github.aj8gh.countdown.util.timer.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private static final int WARM_UPS = 20;

    private static final Deserializer DESERIALIZER = new Deserializer();
    private static final Serializer SERIALIZER = new Serializer();
    private static final Generator GENERATOR = new Generator(new CalculatorV1(), new Timer(), WARM_UPS);
    private static final Solver SOLVER = new SimpleSolver();

    public static void main(String... args) {
        var input = readFromFile(args);
        warmUp();
        SOLVER.solve(input);
        writeToFile();
        logSolver();
    }

    private static List<Integer> readFromFile(String... args) {
        var file = args.length == 0 ? null : args[0];
        return DESERIALIZER.forSolver(file);
    }

    private static void writeToFile() {
        SERIALIZER.serializeSolver(SOLVER.getSolution().getRpn(), SOLVER.getTime());
    }

    private static void warmUp() {
        for (int i = 0; i < WARM_UPS; i++) {
            GENERATOR.generate(ThreadLocalRandom.current().nextInt(5));
            SOLVER.solve(GENERATOR.getQuestionNumbers());
            SOLVER.reset();
            GENERATOR.reset();
        }
    }

    private static void logSolver() {
        LOG.info("""
                        
                        ============================================================================
                        SOLVER
                        Solution:     {} = {}
                        RPN:          {}
                        Attempts:     {}
                        Time:         {} ms
                        Mode:         {}
                        ============================================================================
                        """,
                SOLVER.getSolution(),
                SOLVER.getSolution().getValue(),
                SOLVER.getSolution().getRpn(),
                SOLVER.getAttempts(),
                SOLVER.getTime(),
                SOLVER.getMode()
        );
    }
}
