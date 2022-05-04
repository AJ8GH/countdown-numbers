package io.github.aj8gh.countdown.in.props;

import io.github.aj8gh.countdown.in.InputSupplier;

import java.util.List;

public class PropsInputSupplier implements InputSupplier {
    private final List<Integer> testInput;
    private final int numLarge;

    public PropsInputSupplier(List<Integer> testInput, int numLarge) {
        this.testInput = testInput;
        this.numLarge = numLarge;
    }

    @Override
    public List<Integer> getSolverInput() {
        return testInput;
    }

    @Override
    public int getGeneratorInput() {
        return numLarge;
    }
}
