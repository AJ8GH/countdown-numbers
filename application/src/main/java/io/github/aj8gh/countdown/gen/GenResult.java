package io.github.aj8gh.countdown.gen;

import io.github.aj8gh.countdown.calc.Calculator;
import lombok.Builder;

import java.util.List;

public record GenResult(long attempts,
                        String solution,
                        String rpn,
                        List<Integer> questionNumbers,
                        int target,
                        double time,
                        Calculator.CalculationMode mode) {
    @Builder public GenResult {}
}
