package aj.countdown.runner;

import aj.countdown.domain.Calculation;
import aj.countdown.domain.Calculator;
import aj.countdown.generator.Generator;
import aj.countdown.solver.Solver;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static aj.countdown.generator.Filter.NOT_FIVE;
import static aj.countdown.generator.Filter.ODD;

@Slf4j
public class Main {
    private static final Calculator CALCULATOR = new Calculator();
    private static final Generator GENERATOR = new Generator(CALCULATOR, 5);
    private static final Solver SOLVER = new Solver(CALCULATOR);

    private static final int NUMBER_OF_LARGE = 1;
    private static final int TIME_SCALE = 6;
    private static final int RUNS = 10;
    private static final int WARM_UPS = 10;

    private static final List<List<Integer>> testInputs = List.of(
            List.of(50, 25, 75, 2, 1, 100, 199),
            List.of(6, 100, 4, 6, 8, 2, 752),
            List.of(75, 5, 25, 6, 50, 100, 426)
    );

    static {
        warmUp();
    }

    public static void main(String[] args) {
        GENERATOR.addFilter(ODD).addFilter(NOT_FIVE);
        runSolver();
    }

    private static void runTestInputs() {
        testInputs.forEach(Main::solve);
    }

    private static void runSolver() {
        for (int i = 0; i < RUNS; i++) {
            var target = GENERATOR.generateTarget(NUMBER_OF_LARGE);
            var solution = SOLVER.solve(new ArrayList<>(GENERATOR.getQuestionNumbers()));
            logQuestionAndSolution(target, solution);
            reset();
        }
    }

    private static void solve(List<Integer> input) {
        var solution = SOLVER.solve(new ArrayList<>(input));
        logSolution(input, solution);
        reset();
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

    private static void logSolution(List<Integer> input, Calculation solution) {
        log.info(
                """
                    
                    Question: {}
                    Solver solution: {} = {}, solved in {} ms with {} attempts
                    ***""",
                input,
                solution.getSolution(),
                solution.getResult(),
                BigDecimal.valueOf(SOLVER.getTime()).setScale(TIME_SCALE, RoundingMode.HALF_UP),
                SOLVER.getAttempts()
        );
    }

    private static void warmUp() {
        for (int i = 0; i < WARM_UPS; i++) {
            GENERATOR.generateTarget(NUMBER_OF_LARGE);
            SOLVER.solve(new ArrayList<>(GENERATOR.getQuestionNumbers()));
            reset();
        }
    }

    private static void reset() {
        GENERATOR.fullReset();
        SOLVER.reset();
    }
}
