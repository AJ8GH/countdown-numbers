package io.github.aj8gh.countdown.gen;

import io.github.aj8gh.countdown.calc.Calculation;
import io.github.aj8gh.countdown.util.DifficultyAnalyser;

public class GenAdaptor {
    private final Generator generator;
    private final DifficultyAnalyser difficultyAnalyser;
    private boolean checkDifficulty;
    private Calculation result;

    public GenAdaptor(Generator generator,
                      DifficultyAnalyser difficultyAnalyser) {
        this.generator = generator;
        this.difficultyAnalyser = difficultyAnalyser;
    }

    public Calculation generate(int numberOfLarge) {
        runGenerator(numberOfLarge);
        return getSolution();
    }

    private void runGenerator(int numberOfLarge) {
        this.result = generator.generate(numberOfLarge);
        if (checkDifficulty) {
            while (!difficultyAnalyser.isDifficult(generator.getQuestionNumbers())) {
                generator.setUp();
                this.result = generator.generate(numberOfLarge);
            }
        }
        generator.reset();
    }

    public void setCheckDifficulty(boolean checkDifficulty) {
        this.checkDifficulty = checkDifficulty;
    }

    public Calculation getSolution() {
        return result;
    }

    public void warmup() {
        generator.warmup();
    }
}
