package alvin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NumberGenerator {

    private static final int[] LARGE_NUMBERS = {25, 50, 75, 100};
    private static final int SMALL_NUMBER_CIELING = 9;

    public static List<Integer> generateSmallNums(int smallCount){
        List<Integer> smallNums = new ArrayList<>(smallCount);
        for (int i = 0; i < smallCount; i++) {
            int a = (int) Math.round(Math.random() * SMALL_NUMBER_CIELING + 1);
            smallNums.add(a);
        }
        return smallNums;
    }

    public static List<Integer> generateLargeNums(int largeCount){
        List<Integer> largeNums = new ArrayList<>(largeCount);
        for (int i = 0; i < largeCount; i++) {
            int r = (int) Math.round(Math.random() * 3);
            largeNums.add(LARGE_NUMBERS[r]);
        }
        return largeNums;
    }

    public static List<Integer> generateTestNums(){
        return Arrays.asList(2, 5, 25, 50);
    }

}
