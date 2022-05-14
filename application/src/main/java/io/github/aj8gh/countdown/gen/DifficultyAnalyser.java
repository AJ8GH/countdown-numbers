package io.github.aj8gh.countdown.gen;

import io.github.aj8gh.countdown.sol.Solver;

import java.util.List;

public class DifficultyAnalyser {
    private static final double DEFAULT_DIFFICULTY = 0.0;
    private static final int DEFAULT_RUNS = 100;
    private static final int DEFAULT_WARM_UPS = 0;
    private static final int DEFAULT_MAX_NUMBERS = 6;
    private static final int DEFAULT_MIN_ATTEMPTS = 50_000;

    private final Solver solver;
    private int maxNumbers = DEFAULT_MAX_NUMBERS;
    private int minAttempts = DEFAULT_MIN_ATTEMPTS;

    private int runs = DEFAULT_RUNS;
    private double minDifficulty = DEFAULT_DIFFICULTY;
    private double difficulty = DEFAULT_DIFFICULTY;

    public DifficultyAnalyser(Solver solver) {
        solver.setWarmUps(DEFAULT_WARM_UPS);
        this.solver = solver;
    }

    public boolean isDifficultAttempts(int attempts) {
        return attempts >= minAttempts;
    }

    public boolean isDifficultMaxNumbers(List<Integer> numbers) {
        var difficultRuns = 0;
        var easyRuns = 0;
        for (int i = 0; i < runs; i++) {
            solver.solve(numbers);
            if (solver.getSolution().getNumbers() >= maxNumbers) {
                difficultRuns++;
            } else {
                easyRuns++;
            }
            solver.reset();
            if (1 - easyRuns / (double) runs < minDifficulty) {
                return false;
            }
        }
        this.difficulty = difficultRuns / (double) runs;
        return difficulty >= minDifficulty;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public void setMinDifficulty(double minDifficulty) {
        this.minDifficulty = minDifficulty;
    }

    public void setMaxNumbers(int maxNumbers) {
        this.maxNumbers = maxNumbers;
    }

    public void setMinAttempts(int minAttempts) {
        this.minAttempts = minAttempts;
    }

    public double getDifficulty() {
        return difficulty;
    }
}
