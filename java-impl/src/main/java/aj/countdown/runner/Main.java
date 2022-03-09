package aj.countdown.runner;

import aj.countdown.domain.Calculation;
import aj.countdown.domain.Calculator;
import aj.countdown.generator.Generator;
import aj.countdown.solver.Solver;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import static aj.countdown.generator.Filter.ODD_AND_NOT_FIVE;

@Slf4j
public class Main {
    private static final Calculator CALCULATOR = new Calculator();
    private static final Generator GENERATOR = new Generator(CALCULATOR, 5, ODD_AND_NOT_FIVE);
    private static final Solver SOLVER = new Solver(CALCULATOR);

    private static final int NUMBER_OF_LARGE = 2;
    private static final int TIME_SCALE = 6;
    private static final int RUNS = 10;

    public static void main(String[] args) {
        runSolver();
    }

    public static void runSolver() {
        for (int i = 0; i < RUNS; i++) {
            var target = GENERATOR.generateTarget(NUMBER_OF_LARGE);
            var solution = SOLVER.solve(new ArrayList<>(GENERATOR.getQuestionNumbers()));
            logQuestionAndSolution(target, solution);
            reset();
        }
    }

    private static void logQuestionAndSolution(Calculation target, Calculation solution) {
        log.info(
                """
                    
                    Question: {}
                    Generator solution: {} = {}, generated in {} ms with {} attempts
                    Solver solution: {} = {}, solved in {} ms with {} attempts
                    ***""",
                GENERATOR.getQuestionNumbers(),
                target.getSolution(),
                target.getResult(),
                BigDecimal.valueOf(GENERATOR.getTime()).setScale(TIME_SCALE, RoundingMode.HALF_UP),
                GENERATOR.getAttempts(),
                solution.getSolution(),
                solution.getResult(),
                BigDecimal.valueOf(SOLVER.getTime()).setScale(TIME_SCALE, RoundingMode.HALF_UP),
                SOLVER.getAttempts()
        );
    }

    private static void reset() {
        GENERATOR.fullReset();
        SOLVER.reset();
    }
}
