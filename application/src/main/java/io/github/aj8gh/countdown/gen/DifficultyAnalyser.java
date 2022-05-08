package io.github.aj8gh.countdown.gen;

import io.github.aj8gh.countdown.sol.Solver;

import java.util.List;

public class DifficultyAnalyser {
    private static final double DEFAULT_DIFFICULTY = 0.0;
    private static final int DEFAULT_RUNS = 100;
    private static final int DEFAULT_WARM_UPS = 0;
    private static final int DEFAULT_MAX_NUMBERS = 6;

    private final Solver solver;
    private int maxNumbers = DEFAULT_MAX_NUMBERS;

    private int runs = DEFAULT_RUNS;
    private double minDifficulty = DEFAULT_DIFFICULTY;

    public DifficultyAnalyser(Solver solver) {
        solver.setWarmUps(DEFAULT_WARM_UPS);
        this.solver = solver;
    }

    public boolean isDifficult(List<Integer> numbers) {
        var difficultRuns = 0;
        for (int i = 0; i < runs; i++) {
            solver.solve(numbers);
            if (solver.getSolution().getNumbers() >= maxNumbers) {
                difficultRuns++;
            }
            solver.reset();
        }
        if (difficultRuns == 0) return false;
        return difficultRuns / (double) runs >= minDifficulty;
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
}
