package io.github.aj8gh.countdown.game;

import io.github.aj8gh.countdown.gen.GenAdaptor;
import io.github.aj8gh.countdown.sol.SolAdaptor;
import lombok.Builder;
import lombok.Value;

import java.util.concurrent.ScheduledExecutorService;

@Value
@Builder
public class GamerBuilder {
    ScheduledExecutorService scheduler;
    Serializer serializer;
    GenAdaptor generator;
    SolAdaptor solver;
    Timer timer;
}
