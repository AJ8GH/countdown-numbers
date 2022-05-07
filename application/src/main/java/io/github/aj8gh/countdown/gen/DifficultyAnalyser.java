package io.github.aj8gh.countdown.gen;

import io.github.aj8gh.countdown.calc.Calculation;
import io.github.aj8gh.countdown.sol.Solver;

import java.util.ArrayList;
import java.util.List;

public class DifficultyAnalyser {
    private static final double DEFAULT_DIFFICULTY = 0.0;
    private static final int DEFAULT_RUNS = 100;
    private static final int DEFAULT_WARM_UPS = 0;
    private static final int DEFAULT_MAX_NUMBER_THRESHOLD = 6;
    private static final boolean DEFAULT_CACHING = false;

    private final Solver solver;
    private int maxNumberThreshold = DEFAULT_MAX_NUMBER_THRESHOLD;

    private int runs = DEFAULT_RUNS;
    private double minDifficulty = DEFAULT_DIFFICULTY;
    private double lastDifficulty;

    public DifficultyAnalyser(Solver solver) {
        solver.setCaching(DEFAULT_CACHING);
        solver.setWarmUps(DEFAULT_WARM_UPS);
        solver.setMaxNumberThreshold(DEFAULT_MAX_NUMBER_THRESHOLD);
        this.solver = solver;
    }

    public boolean isDifficult(List<Integer> numbers) {
        var difficultRuns = 0;
        List<Calculation> results = new ArrayList<>();
        for (int i = 0; i < runs; i++) {
            solver.solve(numbers);
            results.add(solver.getSolution());
            if (solver.getSolution().getNumbers() >= maxNumberThreshold) {
                difficultRuns++;
            }
            solver.reset();
        }
        if (difficultRuns == 0) return false;
        this.lastDifficulty = difficultRuns / (double) runs;
        return lastDifficulty >= minDifficulty;
    }

    public double getLastDifficulty() {
        return lastDifficulty;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public void setMinDifficulty(double minDifficulty) {
        this.minDifficulty = minDifficulty;
    }
}
