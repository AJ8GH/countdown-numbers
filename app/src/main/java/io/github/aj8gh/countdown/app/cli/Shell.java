package io.github.aj8gh.countdown.app.cli;

import io.github.aj8gh.countdown.generator.Generator;
import io.github.aj8gh.countdown.solver.Solver;
import io.github.aj8gh.countdown.util.serialisation.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;
import java.util.Scanner;

public class Shell {
    private static final String EXIT_MESSAGE = "\n*** Countdown App Shutting Down ***";
    private static final String PROMPT = ">> Ready for input...\n>> ";
    private static final Logger LOG = LoggerFactory.getLogger(Shell.class);
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final PrintStream OUT = System.out;

    private final Serializer serializer;

    public Shell(Serializer serializer) {
        this.serializer = serializer;
    }

    public String getInput() {
        OUT.print(PROMPT);
        return SCANNER.nextLine();
    }

    void logExitMessage() {
        LOG.info(EXIT_MESSAGE);
    }

    void logGenerator(Generator generator) {
        serializer.serializeGenerator(generator.getTarget().getSolution(),
                generator.getTarget().getValue(), generator.getTime());
        var formattedNumbers = generator.getQuestionNumbers()
                .toString().replaceAll("[^\\d\s]", "");
        OUT.printf("""
                        
                        ============================================================================
                        GENERATOR
                        Question:       %s
                        Method:         %s = %s
                        RPN:            %s
                        Attempts:       %s
                        Time:           %s ms
                        Mode:           %s
                        ============================================================================
                        """,
                formattedNumbers,
                generator.getTarget(),
                generator.getTarget().getValue(),
                generator.getTarget().getRpn(),
                generator.getAttempts(),
                generator.getTime(),
                generator.getMode()
        );
    }

    void logSolver(Solver solver) {
        serializer.serializeSolver(solver.getSolution().getSolution(), solver.getTime());
        OUT.printf("""
                        
                        ============================================================================
                        SOLVER
                        Solution:       %s = %s
                        RPN:            %s
                        Attempts:       %s
                        Time:           %s ms
                        Mode:           %s
                        ============================================================================
                        """,
                solver.getSolution(),
                solver.getSolution().getValue(),
                solver.getSolution().getRpn(),
                solver.getAttempts(),
                solver.getTime(),
                solver.getMode()
        );
    }

    void print(String message) {
        OUT.println(message);
    }
}
