package org.practise.algorithm.leetcode.string.hard;
/**
 * 44. Wildcard Matching
 * Hard
 *
 * Given an input string (s) and a pattern (p), implement wildcard pattern matching with support for '?' and '*' where:
 *
 * '?' Matches any single character.
 * '*' Matches any sequence of characters (including the empty sequence).
 * The matching should cover the entire input string (not partial).
 *
 * Example 1:
 * Input: s = "aa", p = "a"
 * Output: false
 * Explanation: "a" does not match the entire string "aa".
 * Example 2:
 *
 * Input: s = "aa", p = "*"
 * Output: true
 * Explanation: '*' matches any sequence.
 * Example 3:
 *
 * Input: s = "cb", p = "?a"
 * Output: false
 * Explanation: '?' matches 'c', but the second letter is 'a', which does not match 'b'.
 *
 * Constraints:
 * 0 <= s.length, p.length <= 2000
 * s contains only lowercase English letters.
 * p contains only lowercase English letters, '?' or '*'.
 * */
public class RegexPatternMatcherII {
    public boolean isMatch(String s, String p) {
        int sLen = s.length(), pLen = p.length();
        boolean[][] dp = new boolean[pLen + 1][sLen + 1];
        dp[0][0] = true;
        for (int pIdx = 1; pIdx < pLen + 1; pIdx++) {
            if (p.charAt(pIdx - 1) == '*') {
                int sIdx = 1;
                while (!dp[pIdx - 1][sIdx -1] && sIdx < sLen + 1) {
                    sIdx++;
                }
                dp[pIdx][sIdx - 1] = dp[pIdx - 1][sIdx - 1];
                while (sIdx < sLen + 1) {
                    dp[pIdx][sIdx++] = true;
                }
            } else if (p.charAt(pIdx - 1) == '?') {
                for (int sIdx = 1; sIdx < sLen + 1; sIdx++){
                    dp[pIdx][sIdx] = dp[pIdx - 1][sIdx - 1];
                }
            } else {
                for (int sIdx = 1; sIdx < sLen + 1; sIdx++) {
                    dp[pIdx][sIdx] = dp[pIdx - 1][sIdx - 1] && p.charAt(pIdx - 1) == s.charAt(sIdx - 1);
                }
            }
        }
        return dp[pLen][sLen];
    }
}
