package aj.countdown.solver;

import aj.countdown.domain.Calculation;
import aj.countdown.domain.Calculator;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static aj.countdown.domain.Calculator.CalculationMode.INTERMEDIATE;
import static aj.countdown.domain.Calculator.CalculationMode.RUNNING;

public class Solver {
    private static final Clock CLOCK = Clock.systemUTC();
    private static final double MILLIS_IN_SECOND = 1000.0;
    private static final double NANOS_IN_MILLI = 1_000_000.0;

    private final AtomicInteger attempts = new AtomicInteger(1);
    private final Calculator calculator;

    private Calculator.CalculationMode mode = INTERMEDIATE;
    private double time;
    private int switchThreshold = 10_000;

    public Solver(Calculator calculator) {
        this.calculator = calculator;
    }

    public Calculation solve(List<Integer> question) {
        double startTime = getCurrentTime();
        int target = question.get(question.size() - 1);
        Calculation result = calculator.calculateSolution(new ArrayList<>(question), mode);
        while (result.getResult() != target) {
            result = calculator.calculateSolution(new ArrayList<>(question), mode);
            if (attempts.incrementAndGet() == switchThreshold) switchMode();
        }
        this.time = getCurrentTime() - startTime;
        return result;
    }

    public void reset() {
        this.attempts.set(1);
        this.time = 0;
    }

    public void setMode(Calculator.CalculationMode mode) {
        this.mode = mode;
    }

    public double getTime() {
        return time;
    }

    public int getAttempts() {
        return attempts.get();
    }

    public Calculator.CalculationMode getMode() {
        return mode;
    }

    public void setSwitchThreshold(int switchThreshold) {
        this.switchThreshold = switchThreshold;
    }

    private double getCurrentTime() {
        var timeNow = CLOCK.instant();
        var second = timeNow.getEpochSecond();
        var nano = timeNow.getNano();
        return (second * MILLIS_IN_SECOND) + (nano / NANOS_IN_MILLI);
    }

    private void switchMode() {
        this.mode = mode.equals(RUNNING) ? INTERMEDIATE : RUNNING;
    }
}
