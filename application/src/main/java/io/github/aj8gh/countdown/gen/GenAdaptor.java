package io.github.aj8gh.countdown.gen;

import io.github.aj8gh.countdown.calc.Calculation;

public class GenAdaptor {
    private final Generator generator;
    private final DifficultyAnalyser difficultyAnalyser;
    private boolean checkDifficulty;

    public GenAdaptor(Generator generator,
                      DifficultyAnalyser difficultyAnalyser) {
        this.generator = generator;
        this.difficultyAnalyser = difficultyAnalyser;
    }

    public Calculation generate(int numberOfLarge) {
        generator.warmUp();
        generator.generate(numberOfLarge);
        if (checkDifficulty) {
            while (!difficultyAnalyser.isDifficult(generator.getQuestionNumbers())) {
                generator.setUp();
                generator.generate(numberOfLarge);
            }
        }
        return generator.getTarget();
    }

    public Generator getGenerator() {
        return generator;
    }

    public void setCheckDifficulty(boolean checkDifficulty) {
        this.checkDifficulty = checkDifficulty;
    }
}
