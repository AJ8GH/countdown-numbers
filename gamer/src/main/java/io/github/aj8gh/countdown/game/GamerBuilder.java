package io.github.aj8gh.countdown.game;

import io.github.aj8gh.countdown.gen.Generator;
import io.github.aj8gh.countdown.ser.Deserializer;
import io.github.aj8gh.countdown.ser.Serializer;
import io.github.aj8gh.countdown.sol.Solver;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GamerBuilder {
    Deserializer deserializer;
    Serializer serializer;
    Generator generator;
    Solver solver;
    Timer timer;
}
