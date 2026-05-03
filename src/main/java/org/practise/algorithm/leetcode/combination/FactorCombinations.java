package org.practise.algorithm.leetcode.combination;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 254. Factor Combinations
 * Solved
 * Medium
 * Topics
 * Companies
 * Numbers can be regarded as the product of their factors.
 *
 * For example, 8 = 2 x 2 x 2 = 2 x 4.
 * Given an integer n, return all possible combinations of its factors. You may return the answer in any order.
 *
 * Note that the factors should be in the range [2, n - 1].
 *
 *
 *
 * Example 1:
 *
 * Input: n = 1
 * Output: []
 * Example 2:
 *
 * Input: n = 12
 * Output: [[2,6],[3,4],[2,2,3]]
 * Example 3:
 *
 * Input: n = 37
 * Output: []
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10
 */
public class FactorCombinations {
    public List<List<Integer>> getFactors(int n) {
        List<List<Integer>> result = new ArrayList<>();
        LinkedList<Integer> tempList = new LinkedList<Integer>();
        tempList.add(n);
        backtrack(tempList, result);
        return result;
    }

    private void backtrack(LinkedList<Integer> tempList, List<List<Integer>> result) {
        if (tempList.size() > 1) {
            result.add(new ArrayList<>(tempList));
        }
        int lastFactor = tempList.removeLast();
        int i = tempList.isEmpty()? 2 : tempList.peekLast();
        for (; i <= lastFactor / i; i++) {
            if (lastFactor % i == 0) {
                tempList.add(i);
                tempList.add(lastFactor / i);
                backtrack(tempList, result);
                tempList.removeLast();
                tempList.removeLast();
            }
        }
        tempList.add(lastFactor);
    }
}
