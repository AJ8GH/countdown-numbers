package aj.countdown.domain;

import aj.countdown.generator.Operator;
import aj.countdown.solver.Solver;
import it.unimi.dsi.util.XoRoShiRo128PlusRandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static aj.countdown.generator.Operator.DIVIDE;
import static aj.countdown.solver.Solver.SolveMode.RUNNING;
import static java.util.Collections.shuffle;

public class Calculator {
    private static final XoRoShiRo128PlusRandom RANDOM = new XoRoShiRo128PlusRandom();
    private static final List<Operator> OPERATORS = Arrays.asList(Operator.values());

    public Calculation calculate(List<Integer> numbers) {
        List<Calculation> calculations = numbers.stream()
                .map(Calculation::new)
                .toList();

        return calculateTarget(calculations);
    }

    public Calculation calculateSolution(List<Integer> numbers, Solver.SolveMode mode) {
        int target = numbers.remove(numbers.size() - 1);
        List<Calculation> calculations = numbers.stream()
                .map(Calculation::new)
                .toList();

        if (mode.equals(RUNNING)) return calculateRunning(calculations, target);
        return calculateSolution(calculations, target);
    }

    private Calculation calculateRunning(List<Calculation> calculations, int target) {
        calculations = new ArrayList<>(calculations);
        shuffle(calculations);
        var x = calculations.get(0);
        for (int i = 1; i < calculations.size(); i++) {
            if (x.getResult() == target) return x;
            x = calculate(x, calculations.get(i));
        }
        return x;
    }

    private Calculation calculateTarget(List<Calculation> inputs) {
        List<Calculation> results = new ArrayList<>();
        for (int i = 0; i < inputs.size(); i++) {
            Calculation x = inputs.get(i);
            Calculation y = (i == 0 || (i < inputs.size() - 1 && RANDOM.nextBoolean())) ?
                    inputs.get(++i) : results.remove(RANDOM.nextInt(results.size()));
            results.add(calculate(x, y));
        }
        return results.size() == 1 ? results.get(0) : calculateTarget(results);
    }

    private Calculation calculateSolution(List<Calculation> inputs, int target) {
        List<Calculation> results = new ArrayList<>();
        for (int i = 0; i < inputs.size(); i++) {
            Calculation x = inputs.get(i);
            Calculation y = (i == 0 || (i < inputs.size() - 1 && RANDOM.nextBoolean())) ?
                    inputs.get(++i) : results.remove(RANDOM.nextInt(results.size()));
            Calculation result = calculate(x, y);
            if (result.getResult() == target) return result;
            results.add(calculate(x, y));
        }
        return results.size() == 1 ? results.get(0) : calculateTarget(results);
    }

    private Calculation calculate(Calculation x, Calculation y) {
        Calculation result = null;
        while (result == null) {
            result = getResult(getOperator(), x, y);
        }
        return result;
    }

    private Calculation getResult(Operator operator, Calculation x, Calculation y) {
        if (operator.apply(x.getResult(), y.getResult()) == 0)  return null;
        if (operator.equals(DIVIDE)) {
            if (x.getResult() % y.getResult() == 0) return x.calculate(operator, y);
            if (y.getResult() % x.getResult() == 0) return y.calculate(operator, y);
            return null;
        }
        return x.calculate(operator, y);
    }

    private Operator getOperator() {
        return OPERATORS.get(RANDOM.nextInt(OPERATORS.size()));
    }
}
