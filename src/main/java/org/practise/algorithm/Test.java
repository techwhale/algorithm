package org.practise.algorithm;

import java.util.*;

public class Test {
    public List<List<Integer>> permute(int[] nums) {
        List<Integer> values = new ArrayList<>();
        for (int value : nums){
            values.add(value);
        }
        List<List<Integer>> result = new ArrayList<>();
        backtrack(0, values, result);
        return result;
    }

    private void backtrack(int startPosition, List<Integer> values,  List<List<Integer>> result) {
        if (values.size() == startPosition) {
            result.add(new ArrayList(values));
        }
        for (int i = startPosition; i < values.size(); i++) {
            // swap numbers
            Collections.swap(values, startPosition, i);

            backtrack(i + 1, values, result);

            // reverse the change
            Collections.swap(values, startPosition, i);
        }
    }
    public static void main(String args[]) {
        Test obj = new Test();
        int[] nums = {1, 2, 3};
        obj.permute(nums);
    }
}
