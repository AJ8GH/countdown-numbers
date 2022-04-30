package io.github.aj8gh.countdown.generator;

import io.github.aj8gh.countdown.util.calculator.Calculation;
import io.github.aj8gh.countdown.util.calculator.Calculator;
import io.github.aj8gh.countdown.util.timer.Timer;
import it.unimi.dsi.util.XoRoShiRo128PlusRandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntPredicate;

import static io.github.aj8gh.countdown.generator.FilterFactory.Filter.IN_RANGE;
import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode;
import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.INTERMEDIATE;

public class Generator {
    private static final XoRoShiRo128PlusRandom RANDOM = new XoRoShiRo128PlusRandom();
    private static final CalculationMode DEFAULT_MODE = INTERMEDIATE;
    private static final IntPredicate DEFAULT_FILTER = IN_RANGE.getPredicate();
    private static final int DEFAULT_WARMUPS = 20;
    private static final int TOTAL_NUMBERS = 6;
    private static final List<Integer> LARGE_NUMBERS = Arrays.asList(25, 50, 75, 100);

    private final List<Integer> questionNumbers = new ArrayList<>();
    private final AtomicInteger attempts = new AtomicInteger(1);
    private final Map<CalculationMode, Calculator> calculators;
    private final Timer timer = new Timer();

    private Calculator calculator;
    private Queue<Integer> largeNumbers;
    private IntPredicate filter = DEFAULT_FILTER;
    private Set<IntPredicate> filters = new HashSet<>(Set.of(filter));
    private Calculation target;
    private int warmUps = DEFAULT_WARMUPS;

    public Generator(Map<CalculationMode, Calculator> calculators) {
        this.calculators = calculators;
        this.calculator = calculators.get(DEFAULT_MODE);
        setUp();
    }

    public Calculation generate(int numberOfLarge) {
        timer.start();
        generateQuestionNumbers(numberOfLarge);
        this.target = calculateTarget(numberOfLarge);
        questionNumbers.add(target.getValue());
        timer.stop();
        return target;
    }

    private Calculation calculateTarget(int numberOfLarge) {
        var result = calculator.calculateTarget(questionNumbers);
        while (!filter.test(result.getValue())) {
            attempts.incrementAndGet();
            setUp();
            generateQuestionNumbers(numberOfLarge);
            result = calculator.calculateTarget(questionNumbers);
        }
        return result;
    }

    private void generateQuestionNumbers(int numberOfLarge) {
        int numberOfSmall = TOTAL_NUMBERS - numberOfLarge;
        addLargeNumbers(numberOfLarge);
        addSmallNumbers(numberOfSmall);
    }

    private void addLargeNumbers(int numberOfLarge) {
        for (int i = 0; i < numberOfLarge; i++) {
            questionNumbers.add(largeNumbers.remove());
        }
    }

    private void addSmallNumbers(int numberOfSmall) {
        for (int i = 0; i < numberOfSmall; i++) {
            questionNumbers.add(RANDOM.nextInt(10) + 1);
        }
    }

    private void setUp() {
        Collections.shuffle(LARGE_NUMBERS);
        this.largeNumbers = new LinkedList<>(LARGE_NUMBERS);
        this.questionNumbers.clear();
    }

    public Generator addFilter(IntPredicate predicate) {
        if (!filters.contains(predicate)) {
            this.filter = filter.and(predicate);
            filters.add(predicate);
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

    public Calculation getTarget() {
        return target;
    }

    public double getTime() {
        return timer.getLastTime();
    }

    public int getAttempts() {
        return attempts.get();
    }

    public CalculationMode getMode() {
        return calculator.getMode();
    }

    public void setMode(CalculationMode mode) {
        this.calculator = calculators.get(mode);
    }

    public void setTimeScale(int timeScale) {
        timer.setTimescale(timeScale);
    }

    public void setWarmUps(int warmUps) {
        this.warmUps = warmUps;
    }

    public void warmUp() {
        for (int i = 0; i < warmUps; i++) {
            generate(i % 5);
            reset();
        }
    }
}
