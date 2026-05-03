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

    public int longestPalindromeSubseq2(String s) {
        int n = s.length();
        int[][] memo = new int[n][n];
        return lps(s, 0, n - 1, memo);
    }

    private int lps(String s, int i, int j, int[][] memo) {
        if (memo[i][j] != 0) {
            return memo[i][j];
        }
        if (i > j) {
            return 0;
        }
        if (i == j) {
            return 1;
        }

        if (s.charAt(i) == s.charAt(j)) {
            memo[i][j] = lps(s, i + 1, j - 1, memo) + 2;
        } else {
            memo[i][j] = Math.max(lps(s, i + 1, j, memo), lps(s, i, j - 1, memo));
        }
        return memo[i][j];
    }

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
