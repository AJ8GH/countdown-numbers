package io.github.aj8gh.countdown.out;

import io.github.aj8gh.countdown.gen.Generator;
import io.github.aj8gh.countdown.out.console.Console;
import io.github.aj8gh.countdown.out.serial.Serializer;
import io.github.aj8gh.countdown.out.slack.SlackHandler;
import io.github.aj8gh.countdown.sol.Solver;

public interface OutputHandler {
    enum OutputType {
        CONSOLE(Console.class),
        FILE(Serializer.class),
        SLACK(SlackHandler.class);

        private final Class<? extends OutputHandler> handlerType;

        OutputType(Class<? extends OutputHandler> handlerType) {
            this.handlerType = handlerType;
        }

        public Class<? extends OutputHandler> handlerType() {
            return handlerType;
        }
    }

    void handleSolver(Solver solver);
    void handleGenerator(Generator generator);
}
