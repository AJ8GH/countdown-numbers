package io.github.aj8gh.countdown.gen;

import io.github.aj8gh.countdown.calc.Calculation;

import java.util.List;

public class GenAdaptor {
    private final Generator generator;
    private final DifficultyAnalyser difficultyAnalyser;

    private Calculation result;
    private List<Integer> questionNumbers;
    private boolean checkDifficulty;

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
            while (!difficultyAnalyser.isDifficultMaxNumbers(generator.getQuestionNumbers())) {
                generator.setUp();
                this.result = generator.generate(numberOfLarge);
            }
        }
        this.questionNumbers = generator.getQuestionNumbers();
        generator.reset();
    }

    public void setCheckDifficulty(boolean checkDifficulty) {
        this.checkDifficulty = checkDifficulty;
    }

    public List<Integer> getQuestionNumbers() {
        return questionNumbers;
    }

    public Calculation getSolution() {
        return result;
    }

    public void warmup() {
        generator.warmup();
    }
}
