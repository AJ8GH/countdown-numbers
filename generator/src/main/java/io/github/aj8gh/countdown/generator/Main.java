package io.github.aj8gh.countdown.generator;

import io.github.aj8gh.countdown.util.calculator.Calculator;
import io.github.aj8gh.countdown.util.timer.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private static final int WARM_UPS = 20;
    private static final int MIN_LARGE = 0;
    private static final int MAX_LARGE = 4;

    private static final Calculator CALCULATOR = new Calculator();
    private static final Generator GENERATOR = new Generator(CALCULATOR, new Timer(), WARM_UPS);

    static {
        GENERATOR.setMode(Calculator.CalculationMode.RUNNING);
    }

    public static void main(String... args) {
        for (String arg : args) {
            try {
                int numberOfLarge = Integer.parseInt(arg);
                if (inRange(numberOfLarge)) {
                    GENERATOR.generateTarget(numberOfLarge);
                    LOG.info("{}", GENERATOR);
                } else {
                    LOG.warn("{} is not in range {} to {}", arg, MIN_LARGE, MAX_LARGE);
                }
                GENERATOR.reset();
            } catch (Exception e) {
                LOG.error("Error processing {}", arg);
            }
        }
    }

    private static boolean inRange(int numberOfLarge) {
        return numberOfLarge <= MAX_LARGE && numberOfLarge >= MIN_LARGE;
    }
}
