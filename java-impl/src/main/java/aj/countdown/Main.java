package aj.countdown;

import aj.countdown.generator.Generator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    private static final Generator GENERATOR = new Generator();

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            int target = GENERATOR.generateTargetNumber();
            log.info("Target {}, from {}, by {}", target, GENERATOR.getQuestionNumbers(), GENERATOR.getSolution());
            GENERATOR.reInitialize();
        }
    }
}
