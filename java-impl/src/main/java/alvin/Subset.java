package alvin;

import java.util.ArrayList;
import java.util.List;

public class Subset {

    public static void main(String[] args) {
        List<Integer> l = new ArrayList<>();
        l.add(9);
        l.add(5);
        l.add(10);
        l.add(100);
        Subset s = new Subset();
        System.out.println(s.getSubsets(l));
    }

    public List<List<Integer>> getSubsets(List<Integer> elements){
        List<List<Integer>> subsets = new ArrayList<>();
        List<Integer> l;

        int allMasks = (1 << elements.size());
        for (int i = 1; i < allMasks; i++) {
            l = new ArrayList<>();
            for (int j = 0; j < elements.size(); j++) {
                if ((i & (1 << j)) > 0) { //The j-th element is used
                    l.add(elements.get(j));
//                    System.out.println(elements.get(j));
                }
            }
            subsets.add(l);
        }
        return subsets;
    }

}
