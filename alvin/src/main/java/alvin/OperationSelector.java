package alvin;

public class OperationSelector {

    /**
     * This pointer is used for incrementing selectorArray values
     */
    private int incrementPointer = 0;

    /**
     * This pointer is used to access values in the selectorArray
     */
    private int selectorIndex = 0;

    /**
     * Holds ARRAY_SIZE number of operators (0=Add, 1=Subtract, 2=multiple, 3=divide).
     * Each value is accessed using the pointer 'selectorIndex'.
     */
    private int[] operationArray;

    /**
     * Defines the size of the operationArray
     */
    private int arraySize;

    /**
     * Defines the number of math operations. Cannot change.
     */
    private static final int MAX_OPERATION_SIZE = 3;

    private boolean hasMoreOperations = true;

    public OperationSelector(int arraySize){
        operationArray = new int[arraySize];
    }

    private MathOperation selectOperator(int select){
//        return switch (select) {
//            case 0 -> Operations.add;
//            case 1 -> Operations.subtract;
//            case 2 -> Operations.multiply;
//            default -> Operations.divide;
//        };
        switch (select) {
            case 0: return Operations.add;
            case 1: return Operations.subtract;
            case 2: return Operations.multiply;
            default: return Operations.divide;
        }
    }

    /**
     * Selects an operation from the operationArray
     *
     * @return One of the add/subtract/multiple/divide operations
     */
    public MathOperation getOperation(){
        MathOperation mathOperation = selectOperator(operationArray[selectorIndex++]);
//        System.out.println("opArray: " + operationArray[0] + " " + operationArray[1] + " " + operationArray[2] + " " + operationArray[3]);
        if(selectorIndex == operationArray.length){
            selectorIndex = 0;
            incrementNextArrayPosition(incrementPointer);
        }

        return mathOperation;
    }

    /**
     * This method increments the operationArray in a similar fashion to binary (but starting from left to right).
     * The nth value is incremented until maxed, then it is reset and the nth+1 value is incremented once. The nth value then continues to be read/incremented until it is maxed again etc.
     * This is done recursively.
     *
     * Starts: 0000, 1000, 2000, 3000, 0100, 1100, 2100...
     * Ends:   1233, 2233, 3233, 0333, 1333, 2333, 3333
     *
     * @param incrementPointer
     */
    private void incrementNextArrayPosition(int incrementPointer){
        if(incrementPointer == MAX_OPERATION_SIZE){
            hasMoreOperations = false; // 3 3 3 3
            return;
        }
        if(operationArray[incrementPointer] == 3){
            incrementNextArrayPosition(incrementPointer+1);
            operationArray[incrementPointer] = 0;
        }else{
            operationArray[incrementPointer] = operationArray[incrementPointer]+1;
        }
    }

    public boolean hasMoreOperations(){
        return hasMoreOperations;
    }

}
