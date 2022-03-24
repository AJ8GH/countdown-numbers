package aj.countdown.runner;

import aj.countdown.domain.Calculation;
import aj.countdown.generator.Generator;
import aj.countdown.solver.Solver;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
public class Logger {
    private static final int TIME_SCALE = 6;

    public void logQuestionAndSolution(Calculation target, Calculation solution,
                                               Generator generator, Solver solver) {
        log.info(
                """
                    
                    Question: {}
                    Generator solution: {} = {}, generated in {} ms with {} attempts
                    Solver solution: {} = {}, solved in {} ms with {} attempts, mode: {}
                    ***""",
                generator.getQuestionNumbers(),
                target.getSolution(),
                target.getResult(),
                BigDecimal.valueOf(generator.getTime()).setScale(TIME_SCALE, RoundingMode.HALF_UP),
                generator.getAttempts(),
                solution.getSolution(),
                solution.getResult(),
                BigDecimal.valueOf(solver.getTime()).setScale(TIME_SCALE, RoundingMode.HALF_UP),
                solver.getAttempts(),
                solver.getMode()
        );
    }

    public void logSolution(List<Integer> input, Calculation solution,
                                    Solver solver) {
        log.info(
                """
                    
                    Question: {}
                    Solver solution: {} = {}, solved in {} ms with {} attempts, mode: {}
                    ***""",
                input,
                solution.getSolution(),
                solution.getResult(),
                BigDecimal.valueOf(solver.getTime()).setScale(TIME_SCALE, RoundingMode.HALF_UP),
                solver.getAttempts(),
                solver.getMode()
        );
    }
}
