package io.github.aj8gh.countdown.in.props;

import io.github.aj8gh.countdown.in.InputSupplier;

import java.util.List;

public class PropsInputSupplier implements InputSupplier {
    private final List<List<Integer>> solInput;
    private final List<Integer> genInput;

    public PropsInputSupplier(List<List<Integer>> solInput, List<Integer> genInput) {
        this.solInput = solInput;
        this.genInput = genInput;
    }

    @Override
    public List<List<Integer>> getSolverInput() {
        return solInput;
    }

    @Override
    public List<Integer> getGeneratorInput() {
        return genInput;
    }
}
