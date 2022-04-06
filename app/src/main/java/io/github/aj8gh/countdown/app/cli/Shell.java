package io.github.aj8gh.countdown.app.cli;

import io.github.aj8gh.countdown.generator.Generator;
import io.github.aj8gh.countdown.solver.Solver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;
import java.util.Scanner;

public class Shell {
    private static final String EXIT_MESSAGE = "\n*** Countdown App Shutting Down ***";
    private static final String PROMPT = "\n>> Ready for input...\n>> ";

    private static final Logger LOG = LoggerFactory.getLogger(Shell.class);
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final PrintStream OUT = System.out;

    public String getInput() {
        OUT.print(PROMPT);
        return SCANNER.nextLine();
    }

    void logExitMessage() {
        LOG.info(EXIT_MESSAGE);
    }

    void logGenerator(Generator generator) {
        var formattedNumbers = generator.getQuestionNumbers()
                .toString().replaceAll("[^\\d\s]", "");
        OUT.printf("""
                        
                        ============================================================================
                        GENERATOR
                        Question:     %s
                        Method:       %s = %s
                        Attempts:     %s
                        Time:         %s ms
                        Mode:         %s
                        ============================================================================""",
                formattedNumbers,
                generator.getTarget(),
                generator.getTarget().getResult(),
                generator.getAttempts(),
                generator.getTime(),
                generator.getMode()
        );
    }

    void logSolver(Solver solver) {
        OUT.printf("""
                        
                        ============================================================================
                        SOLVER
                        Solution:     %s = %s
                        Attempts:     %s
                        Time:         %s ms
                        Mode:         %s
                        ============================================================================""",
                solver.getSolution(),
                solver.getSolution().getResult(),
                solver.getAttempts(),
                solver.getTime(),
                solver.getMode()
        );
    }

    void print(String message) {
        OUT.println(message);
    }
}
