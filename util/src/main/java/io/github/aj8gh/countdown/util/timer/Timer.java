package io.github.aj8gh.countdown.util.timer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.util.ArrayList;
import java.util.List;

public class Timer {
    private static final Clock CLOCK = Clock.systemUTC();
    private static final double MILLIS_IN_SECOND = 1000;
    private static final double NANOS_IN_MILLI = 1_000_000;
    private static final int DEFAULT_TIMESCALE = 6;

    private final List<Double> times = new ArrayList<>();
    private int timescale = DEFAULT_TIMESCALE;
    private double startTime;

    public void start() {
        this.startTime = getCurrentTime();
    }

    public void stop() {
        if (startTime != 0) {
            times.add(getCurrentTime() - startTime);
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

    private double getCurrentTime() {
        var currentTime = CLOCK.instant();
        var second = currentTime.getEpochSecond();
        var nano = currentTime.getNano();
        return (second * MILLIS_IN_SECOND) + (nano / NANOS_IN_MILLI);
    }

    private double format(double time) {
        return BigDecimal.valueOf(time)
                .setScale(timescale, RoundingMode.HALF_UP).doubleValue();
    }
}
