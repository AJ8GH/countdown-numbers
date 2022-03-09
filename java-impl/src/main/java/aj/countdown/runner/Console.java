package aj.countdown.runner;

import aj.countdown.domain.Calculation;
import aj.countdown.domain.Calculator;
import aj.countdown.generator.Filter;
import aj.countdown.generator.Generator;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

@Slf4j
public class Console {
    private static final Calculator CALCULATOR = new Calculator();
    private static final Generator GENERATOR = new Generator(CALCULATOR, 5, Filter.ODD);
    private static final Scanner SCANNER = new Scanner(System.in);

    private static final int MIN_LARGE = 0;
    private static final int MAX_LARGE = 4;

    public static void main(String[] args) {
        log.info("How many large numbers do you want:");
        while (true) {
            int numberOfLarge = SCANNER.nextInt();
            if (numberOfLarge < MIN_LARGE || numberOfLarge > MAX_LARGE) break;
            Calculation target = GENERATOR.generateTarget(numberOfLarge);
            logResult(target);
            GENERATOR.fullReset();
        }
    }

    private static void logResult(Calculation target) {
        log.info(
                "problem: {}, solution: {}, generated in {} ms with {} attempts",
                GENERATOR.getQuestionNumbers(),
                target.getSolution(),
                GENERATOR.getTime(),
                GENERATOR.getAttempts()
        );
    }
}
