package io.github.aj8gh.countdown.benchmarking;

import java.util.List;

public class TestInputs {
    public static final List<List<Integer>> TEST_LIST = List.of(
            List.of(75, 25, 10, 1, 10, 2, 101),
            List.of(100, 50, 10, 3, 6, 3, 319),
            List.of(50, 100, 3, 10, 1, 4, 166),
            List.of(75, 50, 3, 10, 5, 4, 331),
            List.of(100, 75, 9, 9, 3, 4, 163),
            List.of(25, 100, 2, 9, 7, 5, 253),
            List.of(100, 75, 2, 9, 2, 8, 269),
            List.of(25, 100, 2, 6, 10, 4, 506),
            List.of(75, 100, 8, 7, 1, 6, 566),
            List.of(50, 25, 1, 4, 10, 1, 429)
    );

    public static final List<List<Integer>> TRICKY = List.of(
            List.of(50, 25, 75, 2, 1, 100, 199),
            List.of(6, 100, 4, 6, 8, 2, 752),
            List.of(25, 50, 75, 100, 3, 6, 952),
            List.of(75, 5, 25, 6, 50, 100, 426)
    );
}

// 75 5 25 6 50 100 426
