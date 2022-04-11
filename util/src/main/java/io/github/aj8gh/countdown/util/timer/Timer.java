package io.github.aj8gh.countdown.util.timer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Timer {
    private static final double NANOS_IN_MILLI = 1_000_000;
    private static final int DEFAULT_TIMESCALE = 4;

    private final List<Double> times = new ArrayList<>();
    private int timescale = DEFAULT_TIMESCALE;
    private double startTime;

    public void start() {
        this.startTime = getCurrentMillis();
    }

    public void stop() {
        if (startTime != 0) {
            times.add(getCurrentMillis() - startTime);
            startTime = 0;
        }
    }

    public double getLastTime() {
        if (times.isEmpty()) return -1;
        return format(times.get(times.size() - 1));
    }

    public double getTotalTime() {
        return format(times.stream().reduce(Double::sum).orElse(-1.0));
    }

    public void reset() {
        startTime = 0;
        times.clear();
    }

    public void setTimescale(int timescale) {
        this.timescale = timescale;
    }

    private double getCurrentMillis() {
        return System.nanoTime() / NANOS_IN_MILLI;
    }

    private double format(double time) {
        return BigDecimal.valueOf(time)
                .setScale(timescale, RoundingMode.HALF_UP).doubleValue();
    }
}
