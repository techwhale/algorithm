package org.practise.algorithm.leetcode.math;

/**
 *
 * 357. Count Numbers with Unique Digits
 *
 * Given a non-negative integer n, count all numbers with unique digits, x, where 0 ≤ x < 10n.
 *
 * Example:
 *
 * Input: 2
 * Output: 91
 * Explanation: The answer should be the total numbers in the range of 0 ≤ x < 100,
 *              excluding 11,22,33,44,55,66,77,88,99
 */
public class CountNumbersWithUniqueDigits {
    public int countNumbersWithUniqueDigits(int n) {
        if (n == 0) return 1;
        int result = 10, uniqueDigits = 9, availableDigits = 9;
        while (n > 1) {
            uniqueDigits = uniqueDigits * availableDigits;
            result += uniqueDigits;
            availableDigits--;
            n--;
        }
        return result;
    }
}
