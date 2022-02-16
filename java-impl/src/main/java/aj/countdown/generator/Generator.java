package aj.countdown.generator;

import it.unimi.dsi.util.XoRoShiRo128PlusRandom;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static aj.countdown.generator.Operator.DIVIDE;

@Slf4j
public class Generator {
    private static final XoRoShiRo128PlusRandom RANDOM = new XoRoShiRo128PlusRandom();
    private static final List<Operator> OPERATORS = Arrays.asList(Operator.values());
    private static final List<Integer> LARGE_NUMBERS = Arrays.asList(25, 50, 75, 100);
    private static final int TOTAL_NUMBERS = 6;
    private static final int TOTAL_OPERATIONS = 5;
    private static final short MIN = 100;
    private static final short MAX = 999;

    private final List<Integer> questionNumbers = new ArrayList<>();

    private StringBuilder solutionBuilder;
    private String solution;
    private Queue<Integer> largeNumbers;
    private int numbersLeft;
    private int numberOfLarge;

    public Generator() {
        Collections.shuffle(LARGE_NUMBERS);
        this.largeNumbers = new LinkedList<>(LARGE_NUMBERS);
        this.numberOfLarge = RANDOM.nextInt(5);
        this.numbersLeft = TOTAL_NUMBERS;
        this.solutionBuilder = new StringBuilder();
    }

    public int generateTargetNumber() {
        while (numbersLeft > 0) {
            questionNumbers.add(getNumber());
        }

        while (true) {
            int target = questionNumbers.get(0);
            for (int i = 1; i < TOTAL_OPERATIONS + 1; i++) {
                if (target == 0 || questionNumbers.get(i) == 0) {
                    log.info("What's happened here?? x: {}, y: {}", questionNumbers.get(i), target);
                }
                target = operate(questionNumbers.get(i), target);
            }
            if (isAcceptable(target)) {
                solution = solutionBuilder.toString();
                return target;
            }
            solutionBuilder = new StringBuilder();
            log.info("Back to start...");
        }
    }

    public void reInitialize() {
        Collections.shuffle(LARGE_NUMBERS);
        this.largeNumbers = new LinkedList<>(LARGE_NUMBERS);
        this.numberOfLarge = RANDOM.nextInt(5);
        this.numbersLeft = TOTAL_NUMBERS;
        this.questionNumbers.clear();
        this.solutionBuilder = new StringBuilder();

    }

    private int operate(int x, int y) {
        Operator operator = getOperator();

        while (true) {
            if (applyOperator(operator, x, y) == 0) {
                operator = getOperator();
            } else {
                solutionBuilder.append(x)
                        .append(operator)
                        .append(y)
                        .append(", ");
                return applyOperator(operator, x, y);
            }
        }
    }

    private int applyOperator(Operator operator, int x, int y) {
        if (operator.equals(DIVIDE)) {
            if (x % y == 0) return operator.apply(x, y);
            if (y % x == 0) return operator.apply(y, x);
            return 0;
        }
        return operator.apply(x, y);
    }

    public List<Integer> getQuestionNumbers() {
        return questionNumbers;
    }

    public String getSolution() {
        return solution;
    }

    private boolean isAcceptable(int target) {
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

    private Operator getOperator() {
        return OPERATORS.get(RANDOM.nextInt(OPERATORS.size()));
    }
}
