package aj.countdown;

import it.unimi.dsi.util.XoRoShiRo128PlusRandom;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Getter
public class Generator {
    private static final XoRoShiRo128PlusRandom RANDOM = new XoRoShiRo128PlusRandom();
    private static final List<Integer> LARGE_NUMBER_LIST = Arrays.asList(25, 50, 75, 100);

    private static final short TOTAL_QUESTION_NUMBERS = 6;
    private static final short SMALL_NUMBER_BOUND = 10;
    private static final short SMALL_NUMBER_ORIGIN = 1;
    private static final short NUMBER_OF_LARGE_BOUND = 5;

    private final List<Integer> questionNumbers = new ArrayList<>(TOTAL_QUESTION_NUMBERS);
    private LinkedList<Integer> largeNumberQueue;

    public Generator() {
        shuffleQueue();
    }

    public void generateNumberSet() {
        int numberOfLarge = RANDOM.nextInt(NUMBER_OF_LARGE_BOUND);
        int numberOfSmall = TOTAL_QUESTION_NUMBERS - numberOfLarge;

        for (int i = 0; i < numberOfLarge; i++) {
            questionNumbers.add(largeNumberQueue.remove());
        }

        for (int i = 0; i < numberOfSmall; i++) {
            questionNumbers.add(RANDOM.nextInt(SMALL_NUMBER_BOUND) + SMALL_NUMBER_ORIGIN);
        }
    }

    public void reset() {
        questionNumbers.clear();
        shuffleQueue();
    }

    private void shuffleQueue() {
        Collections.shuffle(LARGE_NUMBER_LIST);
        largeNumberQueue = new LinkedList<>(LARGE_NUMBER_LIST);
    }
}
