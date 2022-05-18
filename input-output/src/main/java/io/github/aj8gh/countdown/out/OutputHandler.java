package io.github.aj8gh.countdown.out;

import io.github.aj8gh.countdown.gen.GenResult;
import io.github.aj8gh.countdown.sol.SolResult;

public interface OutputHandler {
    void handleSolver(SolResult solResult);
    void handleGenerator(GenResult genResult);
    void handleGenInput(int numLarge);
}
