package org.practise.algorithm.leetcode.combination;

import java.util.*;

/**
 * 47. Permutations II
 *
 * Given a collection of numbers, nums, that might contain duplicates, return all possible unique permutations in any order.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,1,2]
 * Output:
 * [[1,1,2],
 *  [1,2,1],
 *  [2,1,1]]
 * Example 2:
 *
 * Input: nums = [1,2,3]
 * Output: [[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 8
 * -10 <= nums[i] <= 10
 */
public class PermutationsII {
    public List<List<Integer>> permuteUnique(int[] nums) {
        int N = nums.length;
        Map<Integer, Integer> count = new HashMap<>();
        for (int num : nums) {
            count.put(num, count.getOrDefault(num, 0) + 1);
        }
        LinkedList<Integer> currList = new LinkedList<>();
        List<List<Integer>> result = new ArrayList<>();
        backtrack(N, currList, count, result);
        return result;
    }

    private void backtrack(int N, LinkedList<Integer> currList, Map<Integer, Integer> count, List<List<Integer>> result) {
        if (N == currList.size()) {
            result.add(new ArrayList<>(currList));
            return;
        }

        for (Map.Entry<Integer, Integer> entry : count.entrySet()) {
            int k = entry.getKey();
            int v = entry.getValue();
            if (v <= 0)
                continue;
            count.put(k, v - 1);
            currList.addLast(k);
            backtrack(N, currList, count, result);
            currList.removeLast();
            count.put(k, v);
        }
    }
}
