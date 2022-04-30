package io.github.aj8gh.countdown.util.rpn;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RpnParserTest {
    private RpnParser rpnParser;

    @BeforeEach
    void setUp() {
        rpnParser = new RpnParser();
    }

    @ParameterizedTest
    @MethodSource(value = "dataSource")
    void parse(Map<Integer, String> data) {
        Integer result = null;
        for (Integer key : data.keySet()) result = key;

        assertEquals(result, rpnParser.parse(data.get(result)));
    }

    static List<Map<Integer, String>> dataSource() {
        return List.of(
                Map.of(240, "4,100,+,75,-,5,*,2,*,50,-"),
                Map.of(240, "2,75,5,-,50,+,*"),
                Map.of(711, "100,50,+,2,+,2,+,75,-,9,*"),
                Map.of(857, "50,25,6,-,*,7,+,100,-"),
                Map.of(857, "25,6,-,4,-,50,*,7,100,+,+")
        );
    }
}
