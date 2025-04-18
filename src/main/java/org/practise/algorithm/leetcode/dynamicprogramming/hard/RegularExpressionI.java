package org.practise.algorithm.leetcode.dynamicprogramming.hard;

/**
 * 10. Regular Expression Matching
 * Hard
 * Topics
 * Companies
 * Given an input string s and a pattern p, implement regular expression matching with support for '.' and '*' where:
 *
 * '.' Matches any single character.
 * '*' Matches zero or more of the preceding element.
 * The matching should cover the entire input string (not partial).
 *
 *
 *
 * Example 1:
 *
 * Input: s = "aa", p = "a"
 * Output: false
 * Explanation: "a" does not match the entire string "aa".
 * Example 2:
 *
 * Input: s = "aa", p = "a*"
 * Output: true
 * Explanation: '*' means zero or more of the preceding element, 'a'. Therefore, by repeating 'a' once, it becomes "aa".
 * Example 3:
 *
 * Input: s = "ab", p = ".*"
 * Output: true
 * Explanation: ".*" means "zero or more (*) of any character (.)".
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 20
 * 1 <= p.length <= 20
 * s contains only lowercase English letters.
 * p contains only lowercase English letters, '.', and '*'.
 * It is guaranteed for each appearance of the character '*', there will be a previous valid character to match.
 */
public class RegularExpressionI {
    public boolean isMatch(String s, String p) {
        Boolean[][] memo = new Boolean[s.length()+ 1][p.length() + 1];
        return isMatch(0,0,s,p,memo);
    }

    private boolean isMatch(int i, int j, String s, String p, Boolean[][] memo) {
        if (memo[i][j] != null) {
            return memo[i][j];
        }
        Boolean ans = null;
        if (j == p.length()) {
            ans =  i == s.length();
        } else {
            boolean firstMatch = i < s.length() && (s.charAt(i) == p.charAt(j) || p.charAt(j) == '.');
            if (j + 1 < p.length() && p.charAt(j + 1) == '*') {
                ans = isMatch(i, j + 2, s, p, memo)   // zero match
                        || firstMatch && isMatch(i + 1, j,s ,p, memo); // check for more match
            } else {
                ans = firstMatch && isMatch(i + 1, j + 1, s, p, memo);
            }
        }
        memo[i][j] = ans;
        return memo[i][j];
    }
}
