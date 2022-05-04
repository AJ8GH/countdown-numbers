package io.github.aj8gh.countdown.in.rand;

import io.github.aj8gh.countdown.gen.Generator;
import io.github.aj8gh.countdown.in.InputSupplier;
import io.github.aj8gh.countdown.util.Random;

import java.util.List;

public class RandomInputSupplier implements InputSupplier {
    private static final Random RANDOM = new Random();
    private static final int NUM_LARGE_BOUND = 5;

    private final Generator generator;

    public RandomInputSupplier(Generator generator) {
        this.generator = generator;
    }

    @Override
    public List<Integer> getSolverInput() {
        generator.generate(getGeneratorInput());
        var input = generator.getQuestionNumbers();
        generator.reset();
        return input;
    }

    @Override
    public int getGeneratorInput() {
        return RANDOM.nextInt(NUM_LARGE_BOUND);
    }
}
