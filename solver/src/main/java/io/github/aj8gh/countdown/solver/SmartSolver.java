package io.github.aj8gh.countdown.solver;

import io.github.aj8gh.countdown.util.calculator.Calculator;
import io.github.aj8gh.countdown.util.calculator.Calculation;
import io.github.aj8gh.countdown.util.timer.Timer;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SmartSolver implements Solver {
    private static final long DEFAULT_MODE_SWITCH_THRESHOLD = 500;

    private final Calculator calculator;
    private final Timer timer;
    private final AtomicInteger attempts = new AtomicInteger(1);

    private Calculation solution;
    private List<Integer> questionNumbers;
    private long modeSwitchThreshold = DEFAULT_MODE_SWITCH_THRESHOLD;

    public SmartSolver(Calculator calculator, Timer timer) {
        this.calculator = calculator;
        this.timer = timer;
    }

    @Override
    public void solve(List<Integer> question) {
//        [75, 5, 25, 6, 50, 100, 426]
//        (((100 + 50) / 25) * (75 - 5)) + 6
//        (((n0 - n1) / n2) * (n4 + n5)) + n3
        // 100 + 50 = 150
        // 150 / 25 = 6
        // 75 - 5 = 70
        // 70 * 6 = 420
        // 420 + 6 = 426
    }

    @Override
    public void reset() {
        this.attempts.set(1);
        timer.reset();
    }

    @Override
    public Calculation getSolution() {
        return solution;
    }

    @Override
    public double getTime() {
        return timer.getLastTime();
    }

    @Override
    public double getTotalTime() {
        return timer.getTotalTime();
    }

    @Override
    public int getAttempts() {
        return attempts.get();
    }

    @Override
    public long getModeSwitchThreshold() {
        return modeSwitchThreshold;
    }

    @Override
    public Calculator.CalculationMode getMode() {
        return calculator.getMode();
    }

    @Override
    public void setMode(Calculator.CalculationMode mode) {
        //
    }

    @Override
    public void setModeSwitchThreshold(long modeSwitchThreshold) {
        this.modeSwitchThreshold = modeSwitchThreshold;
    }

    @Override
    public void setTimeScale(int timeScale) {
        timer.setTimescale(timeScale);
    }
}
