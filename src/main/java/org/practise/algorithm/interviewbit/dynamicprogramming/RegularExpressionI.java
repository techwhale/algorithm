package org.practise.algorithm.interviewbit.dynamicprogramming;

/**
 * Implement wildcard pattern matching with support for '?' and '*'.
 *
 * '?' : Matches any single character.
 * '*' : Matches any sequence of characters (including the empty sequence).
 * The matching should cover the entire input string (not partial).
 *
 * The function prototype should be:
 *
 * int isMatch(const char *s, const char *p)
 * Examples :
 *
 * isMatch("aa","a") → 0
 * isMatch("aa","aa") → 1
 * isMatch("aaa","aa") → 0
 * isMatch("aa", "*") → 1
 * isMatch("aa", "a*") → 1
 * isMatch("ab", "?*") → 1
 * isMatch("aab", "c*a*b") → 0
 * Return 1/0 for this problem.
 */
public class RegularExpressionI {
    public boolean isMatch(String text, String pattern) {
        boolean[][] matched = new boolean[text.length() + 1][pattern.length() + 1];

        matched[0][0] = true;
        for (int j = 1; j <= pattern.length(); j++) {
            if (pattern.charAt(j - 1) == '*')
                matched[0][j] = matched[0][j - 1];
        }
        for (int i = 1; i <= text.length(); i++) {
            char t = text.charAt(i - 1);
            for (int j = 1; j <= pattern.length(); j++) {
                char p = pattern.charAt(j - 1);
                if (p == '*' && (matched[i - 1][j] || matched[i][j - 1])) {
                    matched[i][j] = true;
                } else if ((p == t || p == '?') && matched[i - 1][j - 1]) {
                    matched[i][j] = true;
                }
            }
        }
        return matched[text.length()][pattern.length()];
    }
}
