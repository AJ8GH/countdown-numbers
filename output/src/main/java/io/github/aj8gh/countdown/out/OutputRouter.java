package io.github.aj8gh.countdown.out;

import io.github.aj8gh.countdown.gen.Generator;
import io.github.aj8gh.countdown.sol.Solver;

import java.util.Map;
import java.util.Set;

public class OutputRouter implements OutputHandler {
    private final Set<OutputType> activeHandlers;
    private final Map<OutputType, OutputHandler> handlers;

    public OutputRouter(Set<OutputType> activeHandlers,
                        Map<OutputType, OutputHandler> handlers) {
        this.activeHandlers = activeHandlers;
        this.handlers = handlers;
    }

    @Override
    public void handleSolver(Solver solver) {
        activeHandlers.forEach(type -> handlers.get(type)
                .handleSolver(solver));
    }

    @Override
    public void handleGenerator(Generator generator) {
        activeHandlers.forEach(type -> handlers.get(type)
                .handleGenerator(generator));
    }

    public void enableHandler(OutputType type) {
        activeHandlers.add(type);
    }

    public void disableHandler(OutputType type) {
        activeHandlers.remove(type);
    }
}
