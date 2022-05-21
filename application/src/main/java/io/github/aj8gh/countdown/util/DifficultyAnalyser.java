package io.github.aj8gh.countdown.util;

import io.github.aj8gh.countdown.sol.Solver;

import java.util.List;

public class DifficultyAnalyser {
    public enum Mode {
        ATTEMPTS, NUMBERS
    }

    private final Solver solver;
    private final Mode mode;

    private double minDifficulty;
    private int minAttempts;
    private int maxNumbers;
    private int runs;

    public DifficultyAnalyser(Solver solver, Mode mode) {
        solver.setWarmups(0);
        this.solver = solver;
        this.mode = mode;
    }

    public boolean isDifficult(List<Integer> numbers) {
        return switch (mode) {
            case ATTEMPTS -> isDifficultByAttempts(numbers);
            case NUMBERS -> isDifficultByNumbers(numbers);
        };
    }

    public boolean isDifficultByAttempts(List<Integer> numbers) {
        solver.solve(numbers);
        return solver.getAttempts() >= minAttempts;
    }

    public boolean isDifficultByNumbers(List<Integer> numbers) {
        int difficult = 0;
        for (int i = 0; i < runs; i++) {
            if (solver.solve(numbers).getNumbers() >= maxNumbers) difficult++;
            if (1 - (i + 1 - difficult) / (double) runs < minDifficulty) break;
        }
        solver.reset();
        return difficult / (double) runs >= minDifficulty;
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
}
