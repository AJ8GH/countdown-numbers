package io.github.aj8gh.countdown.generator;

import java.util.Set;
import java.util.function.IntPredicate;

public final class FilterFactory {
    public enum Filter {
        IN_RANGE(FilterFactory::isInRange, "I"),
        ODD(FilterFactory::isOdd, "O"),
        NOT_FIVE(FilterFactory::isNotDivisibleByFive, "5"),
        NOT_TEN(FilterFactory::isNotDivisibleByTen, "10"),
        NOT_ONE_HUNDRED(FilterFactory::isNotOneHundred, "100"),
        PRIME(FilterFactory::isPrime, "P");

        private final IntPredicate predicate;
        private final String key;

        Filter(IntPredicate predicate, String key) {
            this.predicate = predicate;
            this.key = key;
        }

        public IntPredicate getPredicate() {
            return predicate;
        }

        public String key() {
            return key;
        }
    }

    private static final int MIN_TARGET = 100;
    private static final int MAX_TARGET = 999;
    private static final Set<Integer> VALID_PRIMES = Set.of(
            101, 103, 107, 109, 113, 127, 131, 137, 139, 149,
            151, 157, 163, 167, 173, 179, 181, 191, 193, 197,
            199, 211, 223, 227, 229, 233, 239, 241, 251, 257,
            263, 269, 271, 277, 281, 283, 293, 307, 311, 313,
            317, 331, 337, 347, 349, 353, 359, 367, 373, 379,
            383, 389, 397, 401, 409, 419, 421, 431, 433, 439,
            443, 449, 457, 461, 463, 467, 479, 487, 491, 499,
            503, 509, 521, 523, 541, 547, 557, 563, 569, 571,
            577, 587, 593, 599, 601, 607, 613, 617, 619, 631,
            641, 643, 647, 653, 659, 661, 673, 677, 683, 691,
            701, 709, 719, 727, 733, 739, 743, 751, 757, 761,
            769, 773, 787, 797, 809, 811, 821, 823, 827, 829,
            839, 853, 857, 859, 863, 877, 881, 883, 887, 907,
            911, 919, 929, 937, 941, 947, 953, 967, 971, 977,
            983, 991, 997
    );

    private static boolean isOdd(int target) {
        return target % 2 != 0;
    }

    private static boolean isInRange(int target) {
        return target > MIN_TARGET && target < MAX_TARGET;
    }

    private static boolean isNotDivisibleByFive(int target) {
        return target % 5 != 0;
    }

    private static boolean isNotDivisibleByTen(int target) {
        return target % 10 != 0;
    }

    private static boolean isPrime(int target) {
        return VALID_PRIMES.contains(target);
    }

    private static boolean isNotOneHundred(int target) {
        return target != 100;
    }

    private FilterFactory() {}
}
