package io.github.aj8gh.countdown.util;

import it.unimi.dsi.util.XoRoShiRo128PlusRandom;

public class Random {
    private static final XoRoShiRo128PlusRandom RAND = new XoRoShiRo128PlusRandom();

    public int nextInt(int bound) {
        return RAND.nextInt(bound);
    }
}
