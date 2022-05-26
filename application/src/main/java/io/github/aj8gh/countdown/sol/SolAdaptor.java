package io.github.aj8gh.countdown.sol;

import io.github.aj8gh.countdown.calc.Calculation;

import java.util.List;

public class SolAdaptor {
    private final SolutionCache cache;
    private final Solver solver;
    private Calculation solution;
    private boolean caching;

    public SolAdaptor(SolutionCache cache, Solver solver) {
        this.cache = cache;
        this.solver = solver;
    }

    public Calculation solve(List<Integer> inputNumbers) {
        if (isCached(inputNumbers)) return solution;
        this.solution = solver.solve(inputNumbers);
        cache.put(inputNumbers, solution);
        solver.reset();
        return solution;
    }

    private boolean isCached(List<Integer> question) {
        if (caching) {
            var cachedSolution = cache.get(question);
            if (cachedSolution != null) {
                this.solution = cachedSolution;
                return true;
            }
        }
        return false;
    }

    public void setCaching(boolean caching) {
        this.caching = caching;
    }

    public void warmup() {
        solver.warmUp();
    }
}
