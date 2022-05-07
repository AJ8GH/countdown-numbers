package io.github.aj8gh.countdown.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Timer {
    private static final double NANOS_IN_MILLI = 1_000_000;
    private static final int DEFAULT_TIMESCALE = 4;

    private int timescale = DEFAULT_TIMESCALE;
    private long startTime;
    private long time;

    public void start() {
        this.startTime = System.nanoTime();
    }

    public void stop() {
        if (startTime != 0) {
            time += (System.nanoTime() - startTime);
            startTime = 0;
        }
    }

    public double getTime() {
        return format(time / NANOS_IN_MILLI);
    }

    public void reset() {
        this.startTime = 0;
        this.time = 0;
    }

    public void setTimescale(int timescale) {
        this.timescale = timescale;
    }

    private double format(double time) {
        return BigDecimal.valueOf(time)
                .setScale(timescale, RoundingMode.HALF_UP).doubleValue();
    }
}
