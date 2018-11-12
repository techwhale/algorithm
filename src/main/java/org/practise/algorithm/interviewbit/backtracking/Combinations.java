package org.practise.algorithm.interviewbit.backtracking;

import java.util.ArrayList;
import java.util.List;

/**
 * Given two integers n and k, return all possible combinations of k numbers out of 1 2 3 ... n.
 *
 * Make sure the combinations are sorted.
 *
 * To elaborate,
 *
 * Within every entry, elements should be sorted. [1, 4] is a valid entry while [4, 1] is not.
 * Entries should be sorted within themselves.
 * Example :
 * If n = 4 and k = 2, a solution is:
 *
 * [
 *   [1,2],
 *   [1,3],
 *   [1,4],
 *   [2,3],
 *   [2,4],
 *   [3,4],
 * ]
 */
public class Combinations {
    List<List<Integer>> resultList;
    public List<List<Integer>> combine(int n, int k) {
        resultList = new ArrayList<>();
        if (n < k || k <= 0 || n <= 0) return resultList;
        combine(n, 0, k,  new ArrayList<>());
        return resultList;
    }

    private void combine(int n, int i, int k, List<Integer> list) {
        if (k == 0) {
            resultList.add(new ArrayList<>(list));
        }
        for (int start = i + 1; start <= n; start++) {
            list.add(start);
            combine(n, start, k - 1, list);
            list.remove(list.size() - 1);
        }
    }
}
