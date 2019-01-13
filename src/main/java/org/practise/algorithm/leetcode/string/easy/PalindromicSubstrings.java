package org.practise.algorithm.leetcode.string.easy;

/**
 * Given a string, your task is to count how many palindromic substrings in this string.
 *
 * The substrings with different start indexes or end indexes are counted as different substrings even they consist of same characters.
 *
 * Example 1:
 * Input: "abc"
 * Output: 3
 * Explanation: Three palindromic strings: "a", "b", "c".
 * Example 2:
 * Input: "aaa"
 * Output: 6
 * Explanation: Six palindromic strings: "a", "a", "a", "aa", "aa", "aaa".
 * Note:
 * The input string length won't exceed 1000.
 */
public class PalindromicSubstrings {
    public int countSubstrings(String s) {
        int start = 0, count = 0;
        while (start < s.length()) {
            int end = s.length() - 1;
            while (start <= end) {
                if (isPalindrome(s, start, end))
                    count++;
                end--;
            }
            start++;
        }
        return count;
    }

    private boolean isPalindrome(String word, int startIndex, int endIndex) {
        if (startIndex > endIndex || word.length() <= endIndex)
            return false;

        while (startIndex < endIndex) {
            if (word.charAt(startIndex++) != word.charAt(endIndex--))
                return false;
        }
        return true;
    }
}
