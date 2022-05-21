package io.github.aj8gh.countdown.game;

public class Timer {
    public enum Unit {
        MILLI(1_000_000),
        MICRO(1_000),
        NANO(1);

        private final long nanos;

        Unit(long nanosInUnit) {
            this.nanos = nanosInUnit;
        }

        public long getNanos() {
            return nanos;
        }
    }

    private final Unit unit;
    private long startTime;
    private long time;

    public Timer(Unit unit) {
        this.unit = unit;
    }

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
        return time / unit.getNanos();
    }

    public void reset() {
        this.startTime = 0;
        this.time = 0;
    }
}
