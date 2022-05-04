package io.github.aj8gh.countdown.app.test;

import io.github.aj8gh.countdown.calc.Calculator.CalculationMode;

public record Result(CalculationMode mode,
                     double time,
                     int attempts,
                     String solution,
                     int result,
                     String rpn) { }
