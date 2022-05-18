package io.github.aj8gh.countdown.sol;

import io.github.aj8gh.countdown.calc.Calculation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SolutionCache {
    private final Map<Key, Calculation> cache = new HashMap<>();

    public Calculation get(List<Integer> question) {
        var key = new Key(question);
        return cache.get(key);
    }

    public void put(List<Integer> question, Calculation solution) {
        var key = new Key(question);
        cache.put(key, solution);
    }

    private record Key(List<Integer> question) {
        private Key {
            question = new ArrayList<>(question);
            question.sort(Integer::compareTo);
        }

        @Override
        public boolean equals(Object otherKey) {
            if (otherKey == null) return false;
            if (!(otherKey instanceof Key)) return false;
            return question.equals(((Key) otherKey).question);
        }

        @Override
        public int hashCode() {
            return Objects.hash(question);
        }
    }
}
