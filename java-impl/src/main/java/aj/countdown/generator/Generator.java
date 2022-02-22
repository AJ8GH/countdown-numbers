package aj.countdown.generator;

import aj.countdown.domain.Calculation;
import aj.countdown.domain.Calculator;
import it.unimi.dsi.util.XoRoShiRo128PlusRandom;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Slf4j
public class Generator {
    private static final XoRoShiRo128PlusRandom RANDOM = new XoRoShiRo128PlusRandom();
    private static final List<Integer> LARGE_NUMBERS = Arrays.asList(25, 50, 75, 100);
    private static final int TOTAL_NUMBERS = 6;
    private static final short MIN = 100;
    private static final short MAX = 999;

    private final List<Integer> questionNumbers = new ArrayList<>();
    private final Calculator calculator;

    private Queue<Integer> largeNumbers;
    private int numbersLeft;
    private int numberOfLarge;

    public Generator(Calculator calculator) {
        this.calculator = calculator;
        Collections.shuffle(LARGE_NUMBERS);
        this.largeNumbers = new LinkedList<>(LARGE_NUMBERS);
        this.numberOfLarge = RANDOM.nextInt(5);
        this.numbersLeft = TOTAL_NUMBERS;
    }

    public Calculation generateTarget() {
        var target = calculator.calculate(generateQuestionNumbers());
        while (!inRange(target.getResult())) {
            reInitialize();
            target = calculator.calculate(generateQuestionNumbers());
        }
        return target;
    }

    public List<Integer> generateQuestionNumbers() {
        while (numbersLeft > 0) {
            questionNumbers.add(getNumber());
        }
        return questionNumbers;
    }

    public void reInitialize() {
        Collections.shuffle(LARGE_NUMBERS);
        this.largeNumbers = new LinkedList<>(LARGE_NUMBERS);
        this.numberOfLarge = RANDOM.nextInt(5);
        this.numbersLeft = TOTAL_NUMBERS;
        this.questionNumbers.clear();
    }

    public List<Integer> getQuestionNumbers() {
        return questionNumbers;
    }

    private boolean inRange(int target) {
        return target >= MIN && target <= MAX;
    }

    private int getNumber() {
        numbersLeft--;
        if (numberOfLarge == 0) return getSmallNumber();
        if (numberOfLarge == numbersLeft) return getLargeNumber();
        return RANDOM.nextBoolean() ? getLargeNumber() : getSmallNumber();
    }

    private int getSmallNumber() {
        return RANDOM.nextInt(10) + 1;
    }

    private int getLargeNumber() {
        numberOfLarge--;
        return largeNumbers.remove();
    }
}
