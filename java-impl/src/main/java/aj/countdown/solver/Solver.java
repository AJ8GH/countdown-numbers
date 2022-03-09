package aj.countdown.solver;

import aj.countdown.domain.Calculation;
import aj.countdown.domain.Calculator;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class Solver {
    private static final Clock CLOCK = Clock.systemUTC();
    private static final double MILLIS_IN_SECOND = 1000.0;
    private static final double NANOS_IN_MILLI = 1_000_000.0;

    private final AtomicInteger attempts = new AtomicInteger(1);
    private final Calculator calculator;

    private double time;

    public Solver(Calculator calculator) {
        this.calculator = calculator;
    }

    public Calculation solve(List<Integer> question) {
        double startTime = getCurrentTime();
        int target = question.get(question.size() - 1);
        Calculation result = calculator.calculateSolution(question);
        while (result.getResult() != target) {
            result = calculator.calculate(question);
            attempts.incrementAndGet();
            if (attempts.get() % 10_000_000 == 0) log.info("Attempts: {}, Time: {}", attempts.get(), getCurrentTime() - startTime);
        }
        this.time = getCurrentTime() - startTime;
        return result;
    }

    public void reset() {
        attempts.set(1);
        this.time = 0;
    }

    public double getTime() {
        return time;
    }

    public int getAttempts() {
        return attempts.get();
    }

    private double getCurrentTime() {
        var timeNow = CLOCK.instant();
        var second = timeNow.getEpochSecond();
        var nano = timeNow.getNano();
        return (second * MILLIS_IN_SECOND) + (nano / NANOS_IN_MILLI);
    }
}
