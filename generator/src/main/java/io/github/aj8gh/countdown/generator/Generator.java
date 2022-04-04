package io.github.aj8gh.countdown.generator;

import io.github.aj8gh.countdown.util.calculator.Calculation;
import io.github.aj8gh.countdown.util.calculator.Calculator;
import io.github.aj8gh.countdown.util.timer.Timer;
import it.unimi.dsi.util.XoRoShiRo128PlusRandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntPredicate;

import static io.github.aj8gh.countdown.generator.Filter.IN_RANGE;
import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode;
import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.INTERMEDIATE;

public class Generator {
    private static final XoRoShiRo128PlusRandom RANDOM = new XoRoShiRo128PlusRandom();
    private static final IntPredicate DEFAULT_FILTER = IN_RANGE;
    private static final CalculationMode DEFAULT_MODE = INTERMEDIATE;
    private static final List<Integer> LARGE_NUMBERS = Arrays.asList(25, 50, 75, 100);
    private static final int TOTAL_NUMBERS = 6;

    private final Calculator calculator;
    private final Timer timer;
    private final List<Integer> questionNumbers = new ArrayList<>();
    private final AtomicInteger attempts = new AtomicInteger(1);

    private Queue<Integer> largeNumbers;
    private CalculationMode mode = DEFAULT_MODE;
    private IntPredicate filter = DEFAULT_FILTER;
    private Set<IntPredicate> filters = Set.of(DEFAULT_FILTER);
    private Calculation target;

    public Generator(Calculator calculator, Timer timer, int warmUps) {
        this.calculator = calculator;
        this.timer = timer;
        setUp();
        warmUp(warmUps);
    }

    public Calculation generate(int numberOfLarge) {
        timer.start();
        var numbers = generateQuestionNumbers(numberOfLarge);
        var newTarget = calculator.calculate(numbers, mode);
        while (!filter.test(newTarget.getResult())) {
            attempts.incrementAndGet();
            setUp();
            numbers = generateQuestionNumbers(numberOfLarge);
            newTarget = calculator.calculate(numbers, mode);
        }
        questionNumbers.add(newTarget.getResult());
        timer.stop();
        this.target = newTarget;
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
        if (!filters.contains(predicate)) {
            this.filter = filter.and(predicate);
        }
        return this;
    }

    public void resetFilters() {
        this.filter = DEFAULT_FILTER;
        this.filters = Set.of(DEFAULT_FILTER);
    }

    public void reset() {
        setUp();
        attempts.getAndSet(1);
    }

    public List<Integer> getQuestionNumbers() {
        return new ArrayList<>(questionNumbers);
    }

    public double getTime() {
        return timer.getLastTime();
    }

    public int getAttempts() {
        return attempts.get();
    }

    public void setMode(CalculationMode mode) {
        this.mode = mode;
    }

    public void setTimeScale(int timeScale) {
        timer.setTimescale(timeScale);
    }

    @Override
    public String toString() {
        return String.format("""
                                            
                        Question: %s
                        Generator solution: %s = %s, generated in %s ms, %s attempts
                        ***""",
                questionNumbers,
                target.getSolution(),
                target.getResult(),
                getTime(),
                attempts
        );
    }

    private void setUp() {
        Collections.shuffle(LARGE_NUMBERS);
        this.largeNumbers = new LinkedList<>(LARGE_NUMBERS);
        this.questionNumbers.clear();
    }

    private void warmUp(int warmUps) {
        for (int i = 0; i < warmUps; i++) {
            generate(RANDOM.nextInt(5));
            reset();
        }
    }
}
