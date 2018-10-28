package org.practise.algorithm.leetcode.array.medium;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Given a collection of distinct integers, return all possible permutations.
 *
 * Example:
 *
 * Input: [1,2,3]
 * Output:
 * [
 *   [1,2,3],
 *   [1,3,2],
 *   [2,1,3],
 *   [2,3,1],
 *   [3,1,2],
 *   [3,2,1]
 * ]
 */
public class Permutations {
    public List<List<Integer>> permute(int[] nums) {
        if (nums == null || nums.length < 1) return Collections.EMPTY_LIST;
        List<Integer> numList = new ArrayList<Integer>();
        return permute(numList);
    }

    private List<List<Integer>> permute(List<Integer> nums) {
        if (nums.size() <= 1) {
            List<List<Integer>> singleDigitResult = new ArrayList<>();
            singleDigitResult.add(nums);
            return singleDigitResult;
        }
        List<List<Integer>> resultList = new ArrayList<>();
        for (int i = 0; i < nums.size(); i++) {
            int temp = nums.get(i);
            List<Integer> tempList = new ArrayList<>(nums);
            tempList.remove(i);
            List<List<Integer>> currentResult = permute(tempList);
            for (List<Integer> iterateList : currentResult) {
                iterateList.add(0, new Integer(temp));
            }
            resultList.addAll(currentResult);
        }
        return resultList;
    }
}
