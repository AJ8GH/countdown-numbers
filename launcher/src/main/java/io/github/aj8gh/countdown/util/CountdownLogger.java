package io.github.aj8gh.countdown.util;

import io.github.aj8gh.countdown.util.calculator.Calculation;
import io.github.aj8gh.countdown.generator.Generator;
import io.github.aj8gh.countdown.solver.Solver;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
public class CountdownLogger {
    private static final int DEFAULT_TIME_PRECISION = 6;
    private int timePrecision = DEFAULT_TIME_PRECISION;

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
                formatTime(generator.getTime()),
                generator.getAttempts(),
                solution.getSolution(),
                solution.getResult(),
                formatTime(solver.getTime()),
                solver.getAttempts(),
                solver.getMode()
        );
    }

    public void logSolution(List<Integer> input, Calculation solution,
                                    Solver solver) {
        log.info(
                """
                    
                    Question: {}
                    Solver solution: {} = {}, solved in {} ms with {} attempts, mode: {},
                    ***""",
                input,
                solution.getSolution(),
                solution.getResult(),
                formatTime(solver.getTime()),
                solver.getAttempts(),
                solver.getMode()
        );
    }

    public void logTime(String message, double time, int precision) {
        log.info(message, formatTime(time, precision));
    }

    public void setTimePrecision(int timePrecision) {
        this.timePrecision = timePrecision;
    }

    private BigDecimal formatTime(double time) {
        return formatTime(time, timePrecision);
    }

    private BigDecimal formatTime(double time, int precision) {
        return BigDecimal.valueOf(time)
                .setScale(precision, RoundingMode.HALF_UP);
    }
}
