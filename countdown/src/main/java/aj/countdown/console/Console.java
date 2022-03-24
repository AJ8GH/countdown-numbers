package aj.countdown.console;

import aj.countdown.domain.Calculator;
import aj.countdown.generator.Generator;
import aj.countdown.runner.Logger;
import aj.countdown.solver.Solver;
import it.unimi.dsi.util.XoRoShiRo128PlusRandom;

import java.util.Scanner;

public class Console {
    private static final Logger LOG = new Logger();
    private static final Calculator CALCULATOR = new Calculator();
    private static final Generator GENERATOR = new Generator(CALCULATOR, 5);
    private static final Solver SOLVER = new Solver(CALCULATOR);
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final XoRoShiRo128PlusRandom RANDOM = new XoRoShiRo128PlusRandom();

    private static final int WARM_UPS = 5;
    private static final int MIN_LARGE = 0;
    private static final int MAX_LARGE = 4;

    static {
        warmUp();
    }

    public static void main(String[] args) {
        while (true) {
            int numberOfLarge = SCANNER.nextInt();
            if (numberOfLarge < MIN_LARGE || numberOfLarge > MAX_LARGE) break;
            var target = GENERATOR.generateTarget(numberOfLarge);
            var solution = SOLVER.solve(GENERATOR.getQuestionNumbers());
            LOG.logQuestionAndSolution(target, solution, GENERATOR, SOLVER);
            reset();
        }
    }

    private static void warmUp() {
        for (int i = 0; i < WARM_UPS; i++) {
            GENERATOR.generateTarget(RANDOM.nextInt(MAX_LARGE + 1));
            SOLVER.solve(GENERATOR.getQuestionNumbers());
            reset();
        }
    }

    private static void reset() {
        GENERATOR.fullReset();
        SOLVER.reset();
    }
}
