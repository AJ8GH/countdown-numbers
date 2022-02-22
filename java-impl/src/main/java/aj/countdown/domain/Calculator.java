package aj.countdown.domain;

import aj.countdown.generator.Operator;
import it.unimi.dsi.util.XoRoShiRo128PlusRandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static aj.countdown.generator.Operator.DIVIDE;

public class Calculator {
    private static final XoRoShiRo128PlusRandom RANDOM = new XoRoShiRo128PlusRandom();
    private static final List<Operator> OPERATORS = Arrays.asList(Operator.values());

    private final StringBuilder solution = new StringBuilder();

    public Calculation calculate(List<Integer> numbers) {
        List<Calculation> calculations = calculateInts(numbers);
        return calculations.size() == 1 ? calculations.get(0) :
                calculateCalcs(calculations);
    }

    private List<Calculation> calculateInts(List<Integer> numbers) {
        List<Calculation> calculations = new ArrayList<>();
        for (int i = 0; i < numbers.size(); i++) {
            Calculation calculation = null;
            int x = numbers.get(i);
            if (i == 0 || (i < numbers.size() - 1 && RANDOM.nextBoolean())) {
                int y = numbers.get(++i);
                while (calculation == null) {
                    calculation = calculate(x, y);
                }
            } else {
                Calculation calculationY = calculations.get(RANDOM.nextInt(calculations.size()));
                calculations.remove(calculationY);
                while (calculation == null) {
                    calculation = calculate(x, calculationY);
                }
            }
            calculations.add(calculation);
        }
        return calculations;
    }

    private Calculation calculateCalcs(List<Calculation> calculations) {
        Calculation calculation = null;
        Calculation x = calculations.get(0);
        Calculation y = calculations.get(1);
        while (calculation == null) {
            calculation = calculate(x, y);
        }
        if (calculations.size() == 2) return calculation;
        y = calculations.get(2);
        Calculation newCalculation = null;
        while (newCalculation == null) {
            newCalculation = calculate(calculation, y);
        }
        return newCalculation;
    }

    private Calculation calculate(int x, int y) {
        Operator operator = getOperator();
        if (operator.apply(x, y) == 0)  return null;
        if (operator.equals(DIVIDE)) {
            if (x % y == 0) return new Calculation(operator, x, y);
            if (y % x == 0) return new Calculation(operator, y, x);
            return null;
        }
        return new Calculation(operator, x, y);
    }

    private Calculation calculate(int x, Calculation y) {
        Operator operator = getOperator();
        if (operator.apply(x, y.getResult()) == 0)  return null;
        if (operator.equals(DIVIDE)) {
            if (x % y.getResult() == 0) return new Calculation(operator, x, y);
            if (y.getResult() % x == 0) return new Calculation(operator, y, x);
            return null;
        }
        return new Calculation(operator, x, y);
    }

    private Calculation calculate(Calculation x, Calculation y) {
        Operator operator = getOperator();
        if (operator.apply(x.getResult(), y.getResult()) == 0)  return null;
        if (operator.equals(DIVIDE)) {
            if (x.getResult() % y.getResult() == 0) return new Calculation(operator, x, y);
            if (y.getResult() % x.getResult() == 0) return new Calculation(operator, y, x);
            return null;
        }
        return new Calculation(operator, x, y);
    }

    private Operator getOperator() {
        return OPERATORS.get(RANDOM.nextInt(OPERATORS.size()));
    }
}
