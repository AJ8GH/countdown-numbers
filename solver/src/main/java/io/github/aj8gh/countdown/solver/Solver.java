package io.github.aj8gh.countdown.solver;

import io.github.aj8gh.countdown.util.calculator.Calculator;
import io.github.aj8gh.countdown.util.calculator.calculation.Calculation;

import java.util.List;

public interface Solver {
    void solve(List<Integer> question);
    void reset();
    Calculation getSolution();
    double getTime();
    double getTotalTime();
    int getAttempts();
    int getModeSwitchThreshold();
    Calculator.CalculationMode getMode();
    void setMode(Calculator.CalculationMode mode);
    void setModeSwitchThreshold(int modeSwitchThreshold);
    void setTimeScale(int timeScale);
}
