package io.github.aj8gh.countdown.test;

import io.github.aj8gh.countdown.calc.Calculator.CalculationMode;

public record Result(CalculationMode mode,
                     double time,
                     long attempts,
                     String solution,
                     int result,
                     String rpn) { }
