package io.github.aj8gh.countdown.gen;

import io.github.aj8gh.countdown.calc.Calculator;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class GenResult {
    long attempts;
    String solution;
    String rpn;
    List<Integer> questionNumbers;
    int target;
    double time;
    Calculator.CalculationMode mode;
    double difficulty;
}
