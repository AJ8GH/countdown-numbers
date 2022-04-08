package io.github.aj8gh.countdown.util.calculator.calculation;

import io.github.aj8gh.countdown.util.calculator.Operator;

public interface Calculation {
    Calculation calculate(Operator operator, Calculation calculation);
    Calculation calculate(Operator operator, int number);
    int getValue();
    String getSolution();
    String getRpn();
}
