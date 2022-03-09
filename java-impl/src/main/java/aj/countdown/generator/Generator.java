package aj.countdown.generator;

import aj.countdown.domain.Calculation;
import aj.countdown.domain.Calculator;
import it.unimi.dsi.util.XoRoShiRo128PlusRandom;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntPredicate;

import static aj.countdown.generator.Filter.IN_RANGE;

@Slf4j
public class Generator {
    private static final Clock CLOCK = Clock.systemUTC();
    private static final XoRoShiRo128PlusRandom RANDOM = new XoRoShiRo128PlusRandom();

    private static final List<Integer> LARGE_NUMBERS = Arrays.asList(25, 50, 75, 100);
    private static final int TOTAL_NUMBERS = 6;

    private final List<Integer> questionNumbers = new ArrayList<>();
    private final AtomicInteger attempts = new AtomicInteger(0);
    private final Calculator calculator;
    private final IntPredicate filter;

    private double time;
    private Queue<Integer> largeNumbers;

    public Generator(Calculator calculator, int warmUps, IntPredicate filter) {
        this.calculator = calculator;
        this.filter = filter;
        partialReset();
        warmUp(warmUps);
    }

    public Calculation generateTarget(int numberOfLarge) {
        var startTime = getCurrentTime();
        var target = calculator.calculate(generateQuestionNumbers(numberOfLarge));
        while (!IN_RANGE.and(filter).test(target.getResult())) {
            attempts.incrementAndGet();
            partialReset();
            target = calculator.calculate(generateQuestionNumbers(numberOfLarge));
        }
        questionNumbers.add(target.getResult());
        this.time = getCurrentTime() - startTime;
        return target;
    }

    public List<Integer> generateQuestionNumbers(int numberOfLarge) {
        int numberOfSmall = TOTAL_NUMBERS - numberOfLarge;
        for (int i = 0; i < numberOfLarge; i++) {
            try {
                questionNumbers.add(largeNumbers.remove());
            } catch (NoSuchElementException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < numberOfSmall; i++) {
            questionNumbers.add(RANDOM.nextInt(10) + 1);
        }
        return questionNumbers;
    }

    public void fullReset() {
        partialReset();
        attempts.getAndSet(0);
    }

    private void partialReset() {
        Collections.shuffle(LARGE_NUMBERS);
        this.largeNumbers = new LinkedList<>(LARGE_NUMBERS);
        this.questionNumbers.clear();
    }

    public List<Integer> getQuestionNumbers() {
        return questionNumbers;
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
        return (second * 1000) + (nano / 1_000_000.0);
    }

    private void warmUp(int warmUps) {
        for (int i = 0; i < warmUps; i++) {
            generateTarget(RANDOM.nextInt(5));
            fullReset();
        }
    }
}
