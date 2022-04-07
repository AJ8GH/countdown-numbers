package io.github.aj8gh.countdown.solver;

import io.github.aj8gh.countdown.util.calculator.Calculation;
import io.github.aj8gh.countdown.util.calculator.impl.CalculatorImpl;

import java.util.List;

public interface Solver {
    void solve(List<Integer> question);
    void reset();
    Calculation getSolution();
    double getTime();
    double getTotalTime();
    int getAttempts();
    int getModeSwitchThreshold();
    CalculatorImpl.CalculationMode getMode();
    void setMode(CalculatorImpl.CalculationMode mode);
    void setModeSwitchThreshold(int modeSwitchThreshold);
    void setTimeScale(int timeScale);
}
