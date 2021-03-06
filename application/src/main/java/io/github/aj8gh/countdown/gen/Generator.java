package io.github.aj8gh.countdown.gen;

import io.github.aj8gh.countdown.calc.Calculation;
import io.github.aj8gh.countdown.calc.CalculatorManager;
import io.github.aj8gh.countdown.util.Random;

import java.util.*;
import java.util.function.IntPredicate;

import static io.github.aj8gh.countdown.calc.Calculator.CalculationMode;
import static io.github.aj8gh.countdown.gen.Filter.FilterType.IN_RANGE;

public class Generator {
    private static final List<Integer> LARGE_NUMBERS = Arrays.asList(25, 50, 75, 100);
    private static final int MAX_SMALL_NUMBER = 10;
    private static final int TOTAL_NUMBERS = 6;
    private static final int MAX_NUM_LARGE = 4;
    private static final Random RANDOM = new Random();

    private final List<Integer> questionNumbers = new ArrayList<>();
    private final CalculatorManager calculator;

    private long attempts = 1;
    private Queue<Integer> largeNumbers;
    private IntPredicate filter = IN_RANGE.getPredicate();
    private Calculation target;
    private int warmups;

    public Generator(CalculatorManager calculator) {
        this.calculator = calculator;
        setUp();
    }

    public Calculation generate(int numberOfLarge) {
        generateQuestionNumbers(numberOfLarge);
        this.target = calculateTarget(numberOfLarge);
        questionNumbers.add(target.getValue());
        return target;
    }

    private Calculation calculateTarget(int numberOfLarge) {
        var result = calculator.calculateTarget(questionNumbers);
        while (!filter.test(result.getValue())) {
            calculator.adjustMode(++attempts);
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
            questionNumbers.add(RANDOM.nextInt(MAX_SMALL_NUMBER) + 1);
        }
    }

    public void setUp() {
        Collections.shuffle(LARGE_NUMBERS);
        this.largeNumbers = new LinkedList<>(LARGE_NUMBERS);
        this.questionNumbers.clear();
    }

    public void addFilter(IntPredicate predicate) {
        this.filter = filter.and(predicate);
    }

    public void reset() {
        setUp();
        this.attempts = 1;
    }

    public List<Integer> getQuestionNumbers() {
        return new ArrayList<>(questionNumbers);
    }

    public Calculation getTarget() {
        return target;
    }

    public void setMode(CalculationMode mode) {
        calculator.setMode(mode);
    }

    public void setWarmups(int warmups) {
        this.warmups = warmups;
    }

    public void warmup() {
        for (int i = 0; i < warmups; i++) {
            generate(i % MAX_NUM_LARGE);
            reset();
        }
    }
}
