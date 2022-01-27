package aj.countdown;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    private static final Generator GENERATOR = new Generator();

    public static void main(String[] args) {
        GENERATOR.generateNumberSet();
        log.info("{}", GENERATOR.getQuestionNumbers());
        GENERATOR.reset();
    }
}
