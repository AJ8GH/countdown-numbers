package io.github.aj8gh.countdown.in.rand;

import io.github.aj8gh.countdown.gen.GenAdaptor;
import io.github.aj8gh.countdown.in.InputSupplier;
import io.github.aj8gh.countdown.out.OutputHandler;
import io.github.aj8gh.countdown.out.console.ConsoleHandler;
import io.github.aj8gh.countdown.util.Random;

import java.util.List;

public class RandomInputSupplier implements InputSupplier {
    private static final Random RANDOM = new Random();
    private static final int NUM_LARGE_BOUND = 5;
    private static final OutputHandler handler = new ConsoleHandler();

    private final GenAdaptor generator;

    public RandomInputSupplier(GenAdaptor generator) {
        this.generator = generator;
    }

    @Override
    public List<Integer> getSolverInput() {
        handler.handleGenerator(generator.generate(getGeneratorInput()));
        return generator.getResult().getQuestionNumbers();
    }

    @Override
    public int getGeneratorInput() {
        return RANDOM.nextInt(NUM_LARGE_BOUND);
    }
}
