package io.github.aj8gh.countdown.gen;

import io.github.aj8gh.countdown.util.Timer;

public class GenAdaptor {
    private final Generator generator;
    private final DifficultyAnalyser difficultyAnalyser;
    private final Timer timer;

    private boolean checkDifficulty;
    private GenResult result;

    public GenAdaptor(Generator generator,
                      DifficultyAnalyser difficultyAnalyser,
                      Timer timer) {
        this.generator = generator;
        this.difficultyAnalyser = difficultyAnalyser;
        this.timer = timer;
    }

    public GenResult generate(int numberOfLarge) {
        generator.warmUp();
        timer.start();
        runGenerator(numberOfLarge);
        timer.stop();
        return recordResult();
    }

    private void runGenerator(int numberOfLarge) {
        generator.generate(numberOfLarge);
        if (checkDifficulty) {
            while (!difficultyAnalyser.isDifficultMaxNumbers(generator.getQuestionNumbers())) {
                generator.setUp();
                generator.generate(numberOfLarge);
            }
        }
    }

    private GenResult recordResult() {
        this.result = GenResult.builder()
                .questionNumbers(generator.getQuestionNumbers())
                .target(generator.getTarget().getValue())
                .solution(generator.getTarget().getSolution())
                .rpn(generator.getTarget().getRpn())
                .attempts(generator.getAttempts())
                .time(timer.getTime())
                .mode(generator.getMode())
                .difficulty(difficultyAnalyser.getDifficulty())
                .build();
        timer.reset();
        generator.reset();
        return result;
    }

    public void setCheckDifficulty(boolean checkDifficulty) {
        this.checkDifficulty = checkDifficulty;
    }

    public GenResult getResult() {
        return result;
    }

    public Generator getGenerator() {
        return generator;
    }
}
