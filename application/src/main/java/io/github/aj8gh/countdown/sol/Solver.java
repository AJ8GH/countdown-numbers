package io.github.aj8gh.countdown.sol;

import static io.github.aj8gh.countdown.calc.Calculator.CalculationMode;

import io.github.aj8gh.countdown.calc.Calculation;
import io.github.aj8gh.countdown.calc.CalculatorManager;
import io.github.aj8gh.countdown.gen.Generator;
import java.util.ArrayList;
import java.util.List;

public class Solver {
  private final CalculatorManager calculator;
  private final SolutionCache cache;
  private final Generator generator;

  private boolean optimiseNumbers;
  private boolean caching;
  private long attempts;
  private int warmups;
  private Calculation solution;
  private long optimiseNumbersThreshold;

  public Solver(Generator generator,
                CalculatorManager calculator,
                SolutionCache cache) {
    this.generator = generator;
    this.calculator = calculator;
    this.cache = cache;
  }

  public Calculation solve(List<Integer> question) {
    if (isCached(question)) {
      return solution;
    }
    int target = question.remove(question.size() - 1);
    if (containsTarget(question, target)) {
      return new Calculation(target);
    }

    if (optimiseNumbers) {
      for (int i = 0; i < question.size(); i++) {
        var currentQuestion = new ArrayList<>(question);
        currentQuestion.remove(i);
        solution = calculator.calculateSolution(currentQuestion, target);
        while (solution.getValue() != target) {
          solution = calculator.calculateSolution(currentQuestion, target);
          if (solution.getValue() == target) {
            cache.put(currentQuestion, solution);
            return solution;
          }
          if (++attempts > optimiseNumbersThreshold) {
            attempts = 0;
            break;
          }
        }
      }
    }

    setMode(CalculationMode.INTERMEDIATE);
    solution = calculator.calculateSolution(question, target);
    while (solution.getValue() != target) {
      calculator.adjustMode(++attempts);
      solution = calculator.calculateSolution(question, target);
    }
    cache.put(question, solution);
    return solution;
  }

  private boolean isCached(List<Integer> question) {
    if (caching) {
      var cachedSolution = cache.get(question);
      if (cachedSolution != null) {
        this.solution = cachedSolution;
        return true;
      }
    }
    return false;
  }

  private boolean containsTarget(List<Integer> question, int target) {
    return target == 100 && question.contains(target);
  }

  public void warmup() {
    var saveMode = getMode();
    var saveOptimiseNumbers = optimiseNumbers;
    setOptimiseNumbers(false);
    for (int i = 0; i < warmups; i++) {
      generator.generate(i % 5);
      solve(generator.getQuestionNumbers());
      generator.reset();
      reset();
    }
    setOptimiseNumbers(saveOptimiseNumbers);
    setMode(saveMode);
  }

  public void reset() {
    this.attempts = 1;
  }

  public CalculationMode getMode() {
    return calculator.getMode();
  }

  public void setMode(CalculationMode mode) {
    calculator.setMode(mode);
  }

  public Calculation getSolution() {
    return solution;
  }

  public void setSwitchModes(boolean switchModes) {
    calculator.setSwitchModes(switchModes);
  }

  public void setWarmups(int warmups) {
    this.warmups = warmups;
  }

  public void setOptimiseNumbers(boolean optimiseNumbers) {
    this.optimiseNumbers = optimiseNumbers;
  }

  public void setOptimiseNumbersThreshold(long optimiseNumbersThreshold) {
    this.optimiseNumbersThreshold = optimiseNumbersThreshold;
  }

  public void setCaching(boolean caching) {
    this.caching = caching;
  }
}
