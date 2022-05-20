package io.github.aj8gh.countdown.ser;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Deserializer {
    private static final String COMMA = ",";
    private static final String COLON = ":";

    public int generator(String numLarge) {
        return Integer.parseInt(numLarge);
    }

    public List<Integer> solver(String numbers) {
        var questionAndTarget = numbers.split(COLON);
        var questionNumbers = Arrays.stream(questionAndTarget[0].split(COMMA))
                .map(Integer::valueOf)
                .collect(toList());
        questionNumbers.add(Integer.valueOf(questionAndTarget[1]));
        return questionNumbers;
    }
}
