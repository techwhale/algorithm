package org.practise.algorithm.leetcode.dynamicprogramming.practise;

/**
 * Given a string s, find the longest palindromic subsequence's length in s. You may assume that the maximum length of s is 1000.
 *
 * Example 1:
 * Input:
 *
 * "bbbab"
 * Output:
 * 4
 * One possible longest palindromic subsequence is "bbbb".
 * Example 2:
 * Input:
 *
 * "cbbd"
 * Output:
 * 2
 * One possible longest palindromic subsequence is "bb".
 */
public class LongestPalindromicSubsequence {
    public int longestPalindromeSubseq(String s) {
        if (s.length() == 0)
            return 0;
        int[][] dp = new int[s.length()][s.length()];

        for (int i = 0; i < s.length(); i++) {
            dp[i][i] = 1;
        }

        for (int length = 1; length < s.length(); length++) {
            for (int start = 0; start + length < s.length(); start++) {
                if (s.charAt(start) == s.charAt(start + length)) {
                    dp[start][start + length] = 2 + dp[start + 1][start + length - 1];
                } else {
                    dp[start][start + length] = Math.max(dp[start][start + length - 1], dp[start + 1][start + length]);
                }
            }
        }

        return dp[0][s.length()];
    }
}
