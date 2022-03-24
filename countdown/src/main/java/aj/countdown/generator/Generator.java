package aj.countdown.generator;

import aj.countdown.domain.Calculation;
import aj.countdown.domain.Calculator;
import it.unimi.dsi.util.XoRoShiRo128PlusRandom;

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

import static aj.countdown.domain.Calculator.CalculationMode;
import static aj.countdown.domain.Calculator.CalculationMode.INTERMEDIATE;
import static aj.countdown.generator.Filter.IN_RANGE;

public class Generator {
    private static final Clock CLOCK = Clock.systemUTC();
    private static final XoRoShiRo128PlusRandom RANDOM = new XoRoShiRo128PlusRandom();

    private static final List<Integer> LARGE_NUMBERS = Arrays.asList(25, 50, 75, 100);
    private static final int TOTAL_NUMBERS = 6;

    private final List<Integer> questionNumbers = new ArrayList<>();
    private final AtomicInteger attempts = new AtomicInteger(1);
    private final Calculator calculator;

    private IntPredicate filter = IN_RANGE;
    private Queue<Integer> largeNumbers;
    private CalculationMode mode = INTERMEDIATE;
    private double time;

    public Generator(Calculator calculator, int warmUps) {
        this.calculator = calculator;
        partialReset();
        warmUp(warmUps);
    }

    public Calculation generateTarget(int numberOfLarge) {
        var startTime = getCurrentTime();
        var numbers = generateQuestionNumbers(numberOfLarge);
        var target = calculator.calculate(numbers, mode);
        while (!filter.test(target.getResult())) {
            attempts.incrementAndGet();
            partialReset();
            numbers = generateQuestionNumbers(numberOfLarge);
            target = calculator.calculate(numbers, mode);
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

    public Generator addFilter(IntPredicate predicate) {
        this.filter = filter.and(predicate);
        return this;
    }

    public void fullReset() {
        partialReset();
        attempts.getAndSet(1);
    }

    public void setMode(CalculationMode mode) {
        this.mode = mode;
    }

    public List<Integer> getQuestionNumbers() {
        return new ArrayList<>(questionNumbers);
    }

    public double getTime() {
        return time;
    }

    public int getAttempts() {
        return attempts.get();
    }

    private void partialReset() {
        Collections.shuffle(LARGE_NUMBERS);
        this.largeNumbers = new LinkedList<>(LARGE_NUMBERS);
        this.questionNumbers.clear();
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
