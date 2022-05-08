package io.github.aj8gh.countdown;

import io.github.aj8gh.countdown.gen.GenAdaptor;
import io.github.aj8gh.countdown.out.OutputHandler;
import io.github.aj8gh.countdown.sol.SolAdaptor;

public record BaseApp(GenAdaptor genAdaptor,
                      SolAdaptor solAdaptor,
                      OutputHandler outputHandler) {}
