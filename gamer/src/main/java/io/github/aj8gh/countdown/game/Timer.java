package io.github.aj8gh.countdown.game;

public class Timer {
    private static final int NANOS_IN_MICRO = 1_000;

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

    public long getTime() {
        return time / NANOS_IN_MICRO;
    }

    public void reset() {
        this.startTime = 0;
        this.time = 0;
    }
}
