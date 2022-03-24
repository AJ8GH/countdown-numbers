package aj.countdown.runner;

import aj.countdown.domain.Calculator;
import aj.countdown.generator.Generator;
import aj.countdown.solver.Solver;

import java.util.ArrayList;
import java.util.List;

import static aj.countdown.domain.Calculator.CalculationMode.INTERMEDIATE;
import static aj.countdown.domain.Calculator.CalculationMode.RUNNING;

public class Main {
    private static final Logger LOG = new Logger();
    private static final Calculator CALCULATOR = new Calculator();
    private static final Generator GENERATOR = new Generator(CALCULATOR, 5);
    private static final Solver SOLVER = new Solver(CALCULATOR);

    private static final Calculator.CalculationMode SOLVE_MODE = INTERMEDIATE;
    private static final Calculator.CalculationMode GEN_MODE = RUNNING;

    private static final int NUMBER_OF_LARGE = 2;
    private static final int RUNS = 10;
    private static final int WARM_UPS = 10;

    private static final List<List<Integer>> testInputs = List.of(
            List.of(50, 25, 75, 2, 1, 100, 199),
            List.of(6, 100, 4, 6, 8, 2, 752),
            List.of(25, 50, 75, 100, 3, 6, 952),
            List.of(75, 5, 25, 6, 50, 100, 426)
    );

    private static final List<List<Integer>> IMAGE_TESTS = List.of(
            List.of(75, 25, 10, 1, 10, 2, 101),
            List.of(100, 50, 10, 3, 6, 3, 319),
            List.of(50, 100, 3, 10, 1, 4, 166),
            List.of(75, 50, 3, 10, 5, 4, 331),
            List.of(100, 75, 9, 9, 3, 4, 163),
            List.of(25, 100, 2, 9, 7, 5, 253),
            List.of(100, 75, 2, 9, 2, 8, 269),
            List.of(25, 100, 2, 6, 10, 4, 506),
            List.of(75, 100, 8, 7, 1, 6, 566),
            List.of(50, 25, 1, 4, 10, 1, 429)
    );

    static {
        GENERATOR.setMode(GEN_MODE);
        SOLVER.setMode(SOLVE_MODE);
        warmUp();
    }

    public static void main(String... args) {
        runTestInputs();
    }

    private static void runTestInputs() {
        IMAGE_TESTS.forEach(Main::solve);
    }

    private static void runSolver() {
        for (int i = 0; i < RUNS; i++) {
            var target = GENERATOR.generateTarget(NUMBER_OF_LARGE);
            var solution = SOLVER.solve(GENERATOR.getQuestionNumbers());
            LOG.logQuestionAndSolution(target, solution, GENERATOR, SOLVER);
            reset();
        }
    }

    private static void solve(List<Integer> input) {
        var solution = SOLVER.solve(new ArrayList<>(input));
        LOG.logSolution(input, solution, SOLVER);
        reset();
    }

    private static void warmUp() {
        for (int i = 0; i < WARM_UPS; i++) {
            GENERATOR.generateTarget(NUMBER_OF_LARGE);
            SOLVER.solve(GENERATOR.getQuestionNumbers());
            reset();
        }
    }

    private static void reset() {
        GENERATOR.fullReset();
        SOLVER.reset();
        GENERATOR.setMode(GEN_MODE);
        SOLVER.setMode(SOLVE_MODE);
    }
}
