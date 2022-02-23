package aj.countdown.domain;

import aj.countdown.generator.Operator;
import it.unimi.dsi.util.XoRoShiRo128PlusRandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static aj.countdown.generator.Operator.DIVIDE;

public class Calculator {
    private static final XoRoShiRo128PlusRandom RANDOM = new XoRoShiRo128PlusRandom();
    private static final List<Operator> OPERATORS = Arrays.asList(Operator.values());

    public Calculation calculate(List<Integer> numbers) {
        List<Calculation> calculations = numbers.stream().map(Calculation::new)
                .collect(Collectors.toList());
        return calculateTarget(calculations);
    }

    private Calculation calculateTarget(List<Calculation> inputs) {
        List<Calculation> results = new ArrayList<>();
        for (int i = 0; i < inputs.size(); i++) {
            Calculation x = inputs.get(i);
            Calculation y = (i == 0 || (i < inputs.size() - 1 && RANDOM.nextBoolean())) ?
                    inputs.get(++i) : results.get(RANDOM.nextInt(results.size()));
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
            if (x.getResult() % y.getResult() == 0) x.calculate(operator, y);
            if (y.getResult() % x.getResult() == 0) y.calculate(operator, y);
            return null;
        }
        return new Calculation(operator, x, y);
    }

    private Operator getOperator() {
        return OPERATORS.get(RANDOM.nextInt(OPERATORS.size()));
    }
}



//
//    private Calculation calculateRemaining(List<Calculation> calculations) {
//        Calculation calculation = null;
//        Calculation x = calculations.get(0);
//        Calculation y = calculations.get(1);
//        while (calculation == null) {
//            calculation = calculate(x, y);
//        }
//        if (calculations.size() == 2) return calculation;
//        y = calculations.get(2);
//        Calculation newCalculation = null;
//        while (newCalculation == null) {
//            newCalculation = calculate(calculation, y);
//        }
//        return newCalculation;
//    }
//
//    private Calculation calculate(int x, int y) {
//        Operator operator = getOperator();
//        if (operator.apply(x, y) == 0)  return null;
//        if (operator.equals(DIVIDE)) {
//            if (x % y == 0) return new Calculation(operator, x, y);
//            if (y % x == 0) return new Calculation(operator, y, x);
//            return null;
//        }
//        return new Calculation(operator, x, y);
//    }
//
//    private Calculation calculate(int x, Calculation y) {
//        Operator operator = getOperator();
//        if (operator.apply(x, y.getResult()) == 0)  return null;
//        if (operator.equals(DIVIDE)) {
//            if (x % y.getResult() == 0) return new Calculation(operator, x, y);
//            if (y.getResult() % x == 0) return new Calculation(operator, y, x);
//            return null;
//        }
//        return new Calculation(operator, x, y);
//    }
