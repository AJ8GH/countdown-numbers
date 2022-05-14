package io.github.aj8gh.countdown.in.file;

import io.github.aj8gh.countdown.in.InputSupplier;

import java.util.List;

public class FileInputSupplier implements InputSupplier {
    private final Deserializer deserializer;

    public FileInputSupplier(Deserializer deserializer) {
        this.deserializer = deserializer;
    }

    @Override
    public List<List<Integer>> getSolverInput() {
        return deserializer.forSolver();
    }

    @Override
    public List<Integer> getGeneratorInput() {
        return deserializer.forGenerator();
    }
}
