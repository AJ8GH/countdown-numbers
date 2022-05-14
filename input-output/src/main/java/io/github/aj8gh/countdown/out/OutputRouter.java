package io.github.aj8gh.countdown.out;

import io.github.aj8gh.countdown.gen.GenResult;
import io.github.aj8gh.countdown.sol.SolResult;

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
    public void handleSolver(SolResult result) {
        activeHandlers.forEach(type -> handlers.get(type)
                .handleSolver(result));
    }

    @Override
    public void handleGenerator(GenResult result) {
        activeHandlers.forEach(type -> handlers.get(type)
                .handleGenerator(result));
    }

    @Override
    public void handleGenInput(int numLarge) {
        activeHandlers.forEach(type -> handlers.get(type)
                .handleGenInput(numLarge));
    }

    public void enableHandler(OutputType type) {
        activeHandlers.add(type);
    }

    public void disableHandler(OutputType type) {
        activeHandlers.remove(type);
    }
}
