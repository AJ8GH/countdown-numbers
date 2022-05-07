package io.github.aj8gh.countdown;

import io.github.aj8gh.countdown.gen.DifficultyAnalyser;
import io.github.aj8gh.countdown.gen.GenAdaptor;
import io.github.aj8gh.countdown.out.OutputHandler;
import io.github.aj8gh.countdown.sol.Solver;

public record BaseApp(GenAdaptor genAdaptor,
                      Solver solver,
                      OutputHandler outputHandler) {}
