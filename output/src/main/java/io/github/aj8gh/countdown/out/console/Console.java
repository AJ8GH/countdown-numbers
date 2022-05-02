package io.github.aj8gh.countdown.out.console;

import io.github.aj8gh.countdown.gen.Generator;
import io.github.aj8gh.countdown.out.OutputHandler;
import io.github.aj8gh.countdown.sol.Solver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;
import java.util.Scanner;

import static io.github.aj8gh.countdown.out.OutputHandler.OutputType.CONSOLE;

public class Console implements OutputHandler {
    private static final OutputType TYPE = CONSOLE;
    private static final String EXIT_MESSAGE = "\n*** Countdown App Shutting Down ***";
    private static final String PROMPT = ">> Ready for input...\n>> ";
    private static final Logger LOG = LoggerFactory.getLogger(Console.class);
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final PrintStream OUT = System.out;

    public String getInput() {
        OUT.print(PROMPT);
        return SCANNER.nextLine();
    }

    public void logExitMessage() {
        LOG.info(EXIT_MESSAGE);
    }

    @Override
    public void handleGenerator(Generator generator) {
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

    @Override
    public void handleSolver(Solver solver) {
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

    public void print(String message) {
        OUT.println(message);
    }

    public OutputType getType() {
        return TYPE;
    }
}
