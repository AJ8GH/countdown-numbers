package aj.countdown;

import aj.countdown.calculator.Calculator;
import aj.countdown.generator.Generator;
import aj.countdown.solver.Solver;
import aj.countdown.util.CountdownLogger;
import aj.countdown.util.TestInputs;

import java.util.ArrayList;
import java.util.List;

import static aj.countdown.calculator.Calculator.CalculationMode.INTERMEDIATE;
import static aj.countdown.calculator.Calculator.CalculationMode.RUNNING;

public class Main {
    private static final CountdownLogger LOG = new CountdownLogger();
    private static final Calculator CALCULATOR = new Calculator();
    private static final Generator GENERATOR = new Generator(CALCULATOR, 5);
    private static final Solver SOLVER = new Solver(CALCULATOR);

    private static final Calculator.CalculationMode SOLVE_MODE = RUNNING;
    private static final Calculator.CalculationMode GEN_MODE = INTERMEDIATE;

    private static final int NUMBER_OF_LARGE = 2;
    private static final int RUNS = 10;
    private static final int WARM_UPS = 10;

    static {
        GENERATOR.setMode(GEN_MODE);
        SOLVER.setMode(SOLVE_MODE);
        SOLVER.setSwitchThreshold(20_000);
        warmUp();
    }

    public static void main(String... args) {
        runTestInputs();
        LOG.logTime("\n*** Total time: {} ***\n", SOLVER.getTotalTime(), 3);
    }

    private static void runTestInputs() {
        TestInputs.TEST_LIST.forEach(Main::solve);
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
