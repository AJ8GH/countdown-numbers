package io.github.aj8gh.countdown.sol;

import io.github.aj8gh.countdown.calc.Calculator;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SolResult {
    String solution;
    String rpn;
    int target;
    long attempts;
    double time;
    Calculator.CalculationMode mode;
}
