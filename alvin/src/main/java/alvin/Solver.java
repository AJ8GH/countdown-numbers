package alvin;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Solver {

    public static void main(String[] args) {
//        int numToSolve = 0;
        List<Integer> numList = Stream.concat(NumberGenerator.generateLargeNums(4).stream(), NumberGenerator.generateSmallNums(2).stream()).collect(Collectors.toList());

        //TODO pass through every permutation of the number list to the solver
        //TODO pass through every subset and permutation of subset to the solver

//
//        OperationSelector op = new OperationSelector();
//
//        while(op.hasMoreOperations()){
//            System.out.println("1 " + stringify(op.getOperation()) + " 1 " + stringify(op.getOperation()) + " 1 " + stringify(op.getOperation()) + " 1 " + stringify(op.getOperation()) + " 1");
//        }

        solver(numList);

    }

    private static String stringify(MathOperation mo){
        if(mo.calculate(18,2) == 20){
            return "+";
        }else if(mo.calculate(18,2) == 16){
            return "-";
        } else if(mo.calculate(18,2) == 36){
            return "x";
        } else if(mo.calculate(18,2) == 9){
            return "/";
        }
        return "#";
    }

    /**
     * Use multiple threads. As many as possible for the machine
     *
     */
    public static void solver(List<Integer> numList){
        OperationSelector operationSelector = new OperationSelector(numList.size()-1);

        while(operationSelector.hasMoreOperations()){
            int res = numList.get(0);
            for (int i = 1; i < numList.size(); i++) {
                MathOperation mathOperation = operationSelector.getOperation();

                if(cantOperate(mathOperation, res, numList.get(i))){
                    System.out.println("BREAK\n");
                    continue;
                }
                System.out.println(res + " " + stringify(mathOperation) + " " + numList.get(i));
                res = mathOperation.calculate(res, numList.get(i));

                if(i == numList.size()-1){
                    System.out.println("##Countdown: " + res);
                }
            }
        }
    }

    public static boolean cantOperate(MathOperation mo, int a, int b){
        return mo.calculate(a, b) != Math.floor(mo.calculate(a, b));
    }

}
