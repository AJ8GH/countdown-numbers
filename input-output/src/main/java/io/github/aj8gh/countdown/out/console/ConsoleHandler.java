package io.github.aj8gh.countdown.out.console;

import io.github.aj8gh.countdown.gen.Generator;
import io.github.aj8gh.countdown.out.OutputHandler;
import io.github.aj8gh.countdown.sol.Solver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.aj8gh.countdown.out.OutputHandler.OutputType.CONSOLE;

public class ConsoleHandler implements OutputHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ConsoleHandler.class);
    private static final OutputType TYPE = CONSOLE;

    @Override
    public void handleGenerator(Generator generator) {
        var formattedNumbers = generator.getQuestionNumbers()
                .toString().replaceAll("[^\\d\s]", "");
        LOG.info("""
                        
                        ============================================================================
                        GENERATOR
                        Question:       {}
                        Method:         {} = {}
                        RPN:            {}
                        Attempts:       {}
                        Time:           {} ms
                        Mode:           {}
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
        LOG.info("""
                        
                        ============================================================================
                        SOLVER
                        Solution:       {} = {}
                        RPN:            {}
                        Attempts:       {}
                        Time:           {} ms
                        Mode:           {}
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

    public OutputType getType() {
        return TYPE;
    }
}
