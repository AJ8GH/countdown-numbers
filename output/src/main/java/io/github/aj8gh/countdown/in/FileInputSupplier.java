package io.github.aj8gh.countdown.in;

import io.github.aj8gh.countdown.out.file.Deserializer;

import java.util.List;

public class FileInputSupplier implements InputSupplier {
    private final Deserializer deserializer;

    public FileInputSupplier(Deserializer deserializer) {
        this.deserializer = deserializer;
    }

    @Override
    public List<Integer> getSolverInput() {
        return deserializer.forSolver();
    }

    @Override
    public int getGeneratorInput() {
        return deserializer.forGenerator();
    }
}
