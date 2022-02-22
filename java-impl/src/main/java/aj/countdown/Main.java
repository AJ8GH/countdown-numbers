package aj.countdown;

import aj.countdown.domain.Calculation;
import aj.countdown.domain.Calculator;
import aj.countdown.generator.Generator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    private static final Calculator CALCULATOR = new Calculator();
    private static final Generator GENERATOR = new Generator(CALCULATOR);

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            Calculation target = GENERATOR.generateTarget();
            log.info(
                    "Target: {}, generated from: {}, solution: {}",
                    target.getResult(),
                    GENERATOR.getQuestionNumbers(),
                    target.getSolution()
            );

            GENERATOR.reInitialize();
        }
    }
}
