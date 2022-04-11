package io.github.aj8gh.countdown.util.serialisation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RpnParserTest {
    private RpnParser rpnParser;

    @BeforeEach
    void setUp() {
        rpnParser = new RpnParser();
    }

    @Test
    void parse() {
        assertEquals(240, rpnParser.parse("4,100,+,75,-,5,*,2,*,50,-"));
        assertEquals(240, rpnParser.parse("2,75,5,-,50,+,*"));
        assertEquals(711, rpnParser.parse("100,50,+,2,+,2,+,75,-,9,*"));
        assertEquals(857, rpnParser.parse("50,25,6,-,*,7,+,100,-"));
        assertEquals(857, rpnParser.parse("25,6,-,4,-,50,*,7,100,+,+"));
    }
}
