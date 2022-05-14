package io.github.aj8gh.countdown.in.rand;

import io.github.aj8gh.countdown.gen.GenAdaptor;
import io.github.aj8gh.countdown.in.InputSupplier;
import io.github.aj8gh.countdown.util.Random;

import java.util.ArrayList;
import java.util.List;

public class RandomInputSupplier implements InputSupplier {
    private static final Random RANDOM = new Random();
    private static final int NUM_LARGE_BOUND = 5;

    private final GenAdaptor generator;
    private final int runs;

    public RandomInputSupplier(GenAdaptor generator, int runs) {
        this.generator = generator;
        this.runs = runs;
    }

    @Override
    public List<List<Integer>> getSolverInput() {
        List<List<Integer>> inputs = new ArrayList<>();
        for (int input : getGeneratorInput()) {
            inputs.add(generator.generate(input).getQuestionNumbers());
        }
        return inputs;
    }

    @Override
    public List<Integer> getGeneratorInput() {
        List<Integer> inputs = new ArrayList<>();
        for (int i = 0; i < runs; i++) {
            inputs.add(RANDOM.nextInt(NUM_LARGE_BOUND));
        }
        return inputs;
    }
}
