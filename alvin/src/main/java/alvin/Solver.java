package alvin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Solver {

    private int targetNumber;
    private boolean targetFound = false;

    public static void main(String[] args) {
        Solver solver = new Solver(105);

//        List<Integer> numList = Stream.concat(NumberGenerator.generateLargeNums(1).stream(), NumberGenerator.generateSmallNums(2).stream()).collect(Collectors.toList());
        List<Integer> numList = NumberGenerator.generateTestNums();
        System.out.println("### Original list ###");
        System.out.println(numList);
        System.out.println(numList.size() + "\n");

        List<List<Integer>> numListSubsets;
        List<List<Integer>> numListPerms = new ArrayList<>();

        //get subsets (subset will include whole set)
        numListSubsets = new Subset().getSubsets(numList)
                .stream().distinct().collect(Collectors.toList()); //remove identical lists (will have duplicate solutions)
        System.out.println("### Subset list ###");
        System.out.println(numListSubsets);
        System.out.println(numListSubsets.size() + "\n");


        //run subset through permuatations
        for (List<Integer> list : numListSubsets) {
            numListPerms.addAll(new Permutation().getAllPermutations(list));
        }
        System.out.println("### Permutations & subsets list ###");
        System.out.println(numListPerms);
        System.out.println(numListPerms.size() + "\n");

        //pass new subset+perms list into solver
        int n = 0;
        while(!solver.targetFound){

            solver.solve(numListPerms.get(n));

            if(n+1 == numListPerms.size()){
                System.out.println("#####   TARGET NOT FOUND    #####");
                break;
            }else{
                n++;
            }
        }
    }

    public Solver(int targetNumber){
        this.targetNumber = targetNumber;
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
    public void solve(List<Integer> numList){
        OperationSelector operationSelector = new OperationSelector(numList.size()-1);

        while(operationSelector.hasMoreOperations()){
            int res = numList.get(0);
            if(numList.size()==1){
                System.out.println("##Countdown: " + res);
                break;
            }
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
