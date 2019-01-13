package org.practise.algorithm.basics.dynamicprogramming;

public class SubsetProblem {
    public boolean subsetExist(int[] arr, int target) {
        boolean[] exist = new boolean[target + 1];
        exist[0] = true;
        for (int i = 0; i < arr.length; i++) {
            int val = arr[i];
            for (int j = val; j <= target; j++) {
                exist[j] = exist[j] || exist[j - val];
            }
        }
        return exist[target];
    }
}
