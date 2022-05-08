package io.github.aj8gh.countdown.out.console;

import io.github.aj8gh.countdown.gen.GenResult;
import io.github.aj8gh.countdown.out.OutputHandler;
import io.github.aj8gh.countdown.sol.SolResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.aj8gh.countdown.out.OutputHandler.OutputType.CONSOLE;

public class ConsoleHandler implements OutputHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ConsoleHandler.class);
    private static final OutputType TYPE = CONSOLE;

    @Override
    public void handleGenerator(GenResult result) {
        var formattedNumbers = result.getQuestionNumbers()
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
                result.getSolution(),
                result.getTarget(),
                result.getRpn(),
                result.getAttempts(),
                result.getTime(),
                result.getMode()
        );
    }

    @Override
    public void handleSolver(SolResult result) {
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
                result.getSolution(),
                result.getTarget(),
                result.getRpn(),
                result.getAttempts(),
                result.getTime(),
                result.getMode()
        );
    }

    public OutputType getType() {
        return TYPE;
    }
}
