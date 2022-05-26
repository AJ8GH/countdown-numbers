package io.github.aj8gh.countdown.gen;

import io.github.aj8gh.countdown.calc.Calculation;
import io.github.aj8gh.countdown.util.DifficultyAnalyser;

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
        var result = generator.generate(numberOfLarge);
        if (checkDifficulty) {
            while (!difficultyAnalyser.isDifficult(generator.getQuestionNumbers())) {
                generator.setUp();
                result = generator.generate(numberOfLarge);
            }
        }
        generator.reset();
        return result;
    }

    public void setCheckDifficulty(boolean checkDifficulty) {
        this.checkDifficulty = checkDifficulty;
    }

    public void warmup() {
        generator.warmup();
    }
}
