package org.practise.algorithm.leetcode.dfs.medium;

import java.util.ArrayList;
import java.util.List;

/**
 * 967. Numbers With Same Consecutive Differences
 * Medium
 *
 * Given two integers n and k, return an array of all the integers of length n where the difference between every two consecutive digits is k. You may return the answer in any order.
 *
 * Note that the integers should not have leading zeros. Integers as 02 and 043 are not allowed.
 *
 * Example 1:
 *
 * Input: n = 3, k = 7
 * Output: [181,292,707,818,929]
 * Explanation: Note that 070 is not a valid number, because it has leading zeroes.
 * Example 2:
 *
 * Input: n = 2, k = 1
 * Output: [10,12,21,23,32,34,43,45,54,56,65,67,76,78,87,89,98]
 *
 * Constraints:
 *
 * 2 <= n <= 9
 * 0 <= k <= 9
 */
public class SameConsecutiveDifference {
    public int[] numsSameConsecDiff(int n, int k) {
        List<Integer> result = new ArrayList<>();
        for (int i = 1; i <= 9; i++){
            numsSameConsecDiff(n - 1, k, i, result);
        }
        int[] copyResult = new int[result.size()];
        for (int i = 0; i < result.size() ; i++){
            copyResult[i] = result.get(i);
        }
        return copyResult;
    }

    private void numsSameConsecDiff(int n, int k, int num, List<Integer> result){
        if (n == 0) {
            result.add(num);
            return;
        }
        int lastDigit = num % 10;
        List<Integer> nextDigits = new ArrayList<>();
        nextDigits.add(lastDigit + k);
        if (k != 0) {
            nextDigits.add(lastDigit - k);
        }
        for (int digit : nextDigits){
            if (0 <= digit && digit <= 9){
                numsSameConsecDiff(n - 1, k, num * 10 + digit, result);
            }
        }
    }
}
