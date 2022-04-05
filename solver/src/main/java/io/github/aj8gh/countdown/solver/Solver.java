package io.github.aj8gh.countdown.solver;

import io.github.aj8gh.countdown.util.calculator.Calculation;
import io.github.aj8gh.countdown.util.calculator.Calculator;

import java.util.List;

public interface Solver {
    Calculation solve(List<Integer> question);
    void reset();
    double getTime();
    double getTotalTime();
    int getAttempts();
    Calculator.CalculationMode getMode();
    void setMode(Calculator.CalculationMode mode);
    void setModeSwitchThreshold(int modeSwitchThreshold);
    void setTimeScale(int timeScale);
    void log();
}
