package io.github.aj8gh.countdown.generator;

import io.github.aj8gh.countdown.util.calculator.Calculator;
import io.github.aj8gh.countdown.util.calculator.Calculation;
import io.github.aj8gh.countdown.util.calculator.impl.IntermediateCalculator;
import io.github.aj8gh.countdown.util.calculator.impl.RecursiveCalculator;
import io.github.aj8gh.countdown.util.calculator.impl.SequentialCalculator;
import io.github.aj8gh.countdown.util.timer.Timer;
import it.unimi.dsi.util.XoRoShiRo128PlusRandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntPredicate;

import static io.github.aj8gh.countdown.generator.Filter.IN_RANGE;
import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.INTERMEDIATE;
import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.RECURSIVE;
import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode.SEQUENTIAL;
import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode;

public class Generator {
    private static final XoRoShiRo128PlusRandom RANDOM = new XoRoShiRo128PlusRandom();
    private static final CalculationMode DEFAULT_MODE = SEQUENTIAL;
    private static final IntPredicate DEFAULT_FILTER = IN_RANGE;
    private static final List<Integer> LARGE_NUMBERS = Arrays.asList(25, 50, 75, 100);
    private static final int TOTAL_NUMBERS = 6;

    private final Timer timer = new Timer();
    private final List<Integer> questionNumbers = new ArrayList<>();
    private final AtomicInteger attempts = new AtomicInteger(1);
    private final Map<CalculationMode, Calculator> calculators = Map.of(
            SEQUENTIAL, new SequentialCalculator(),
            INTERMEDIATE, new IntermediateCalculator(),
            RECURSIVE, new RecursiveCalculator());

    private Calculator calculator = calculators.get(DEFAULT_MODE);
    private Queue<Integer> largeNumbers;
    private IntPredicate filter = DEFAULT_FILTER;
    private Set<IntPredicate> filters = Set.of(DEFAULT_FILTER);
    private Calculation target;

    public Generator() {
        setUp();
    }

    public Calculation generate(int numberOfLarge) {
        timer.start();
        var numbers = generateQuestionNumbers(numberOfLarge);
        var newTarget = calculator.calculateTarget(numbers);
        while (!filter.test(newTarget.getValue())) {
            attempts.incrementAndGet();
            setUp();
            numbers = generateQuestionNumbers(numberOfLarge);
            newTarget = calculator.calculateTarget(numbers);
        }
        questionNumbers.add(newTarget.getValue());
        this.target = newTarget;
        timer.stop();
        return target;
    }

    public List<Integer> generateQuestionNumbers(int numberOfLarge) {
        int numberOfSmall = TOTAL_NUMBERS - numberOfLarge;
        for (int i = 0; i < numberOfLarge; i++) {
            questionNumbers.add(largeNumbers.remove());
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

    public void warmUp(int warmUps) {
        for (int i = 0; i < warmUps; i++) {
            generate(i % 5);
            reset();
        }
    }

    private void setUp() {
        Collections.shuffle(LARGE_NUMBERS);
        this.largeNumbers = new LinkedList<>(LARGE_NUMBERS);
        this.questionNumbers.clear();
    }
}
