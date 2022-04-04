package alvin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Permutation {

    private List<List<Integer>> perms = new ArrayList<>();

    public List<List<Integer>> getAllPermutations(List<Integer> numList){
        Integer[] arr = numList.toArray(new Integer[numList.size()]);
        addAllRecursive(arr);
        return getPerms();
    }

    private void addAllRecursive(Integer[] elements) {
        addAllRecursive(elements.length, elements);
    }

    private void addAllRecursive(int n, Integer[] elements) {

        if(n == 1) {
            addPermutation(elements);
        } else {
            for(int i = 0; i < n-1; i++) {
                addAllRecursive(n - 1, elements);
                if(n % 2 == 0) {
                    swap(elements, i, n-1);
                } else {
                    swap(elements, 0, n-1);
                }
            }
            addAllRecursive(n - 1, elements);
        }
    }

    private void swap(Integer[] elements, int a, int b) {
        int tmp = elements[a];
        elements[a] = elements[b];
        elements[b] = tmp;
    }

    private void addPermutation(Integer[] elements) {
        perms.add(Arrays.stream(elements).collect(Collectors.toList()));
    }

    public List<List<Integer>> getPerms(){
        return perms;
    }

    public static void main(String[] argv) {
        Integer[] elements = {1,2,3,4};

        Permutation perm = new Permutation();
        perm.addAllRecursive(elements);
        System.out.println(perm.perms);
        System.out.println(perm.perms.size());
    }
}