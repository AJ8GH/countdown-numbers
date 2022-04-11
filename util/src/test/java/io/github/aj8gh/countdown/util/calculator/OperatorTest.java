package io.github.aj8gh.countdown.util.calculator;

import org.junit.jupiter.api.Test;

import static io.github.aj8gh.countdown.util.calculator.Operator.ADD;
import static io.github.aj8gh.countdown.util.calculator.Operator.DIVIDE;
import static io.github.aj8gh.countdown.util.calculator.Operator.MULTIPLY;
import static io.github.aj8gh.countdown.util.calculator.Operator.SUBTRACT;
import static org.junit.jupiter.api.Assertions.*;

class OperatorTest {
    @Test
    void divide() {
        assertEquals(3, DIVIDE.apply(6, 2));
        assertEquals(0, DIVIDE.apply(1, 2));
        assertEquals(0, DIVIDE.apply(5, 3));
        assertEquals(0, DIVIDE.apply(1, 0));
        assertEquals(0, DIVIDE.apply(0, 1));
    }

    @Test
    void subtract() {
        assertEquals(4, SUBTRACT.apply(6, 2));
        assertEquals(0, SUBTRACT.apply(1, 2));
    }

    @Test
    void add() {
        assertEquals(8, ADD.apply(6, 2));
    }

    @Test
    void multiply() {
        assertEquals(12, MULTIPLY.apply(6, 2));
    }
}
