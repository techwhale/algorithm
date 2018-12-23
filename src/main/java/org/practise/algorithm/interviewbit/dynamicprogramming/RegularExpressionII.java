package org.practise.algorithm.interviewbit.dynamicprogramming;

/**
 * Implement regular expression matching with support for '.' and '*'.
 *
 * '.' Matches any single character.
 * '*' Matches zero or more of the preceding element.
 *
 * The matching should cover the entire input string (not partial).
 *
 * The function prototype should be:
 *
 * int isMatch(const char *s, const char *p)
 * Some examples:
 *
 * isMatch("aa","a") → 0
 * isMatch("aa","aa") → 1
 * isMatch("aaa","aa") → 0
 * isMatch("aa", "a*") → 1
 * isMatch("aa", ".*") → 1
 * isMatch("ab", ".*") → 1
 * isMatch("aab", "c*a*b") → 1
 * Return 0 / 1 ( 0 for false, 1 for true ) for this problem
 */
public class RegularExpressionII {
    // DO NOT MODIFY THE ARGUMENTS WITH "final" PREFIX. IT IS READ ONLY
    public boolean isMatch(final String text, final String pattern) {
        boolean[][] matched = new boolean[text.length() + 1][pattern.length() + 1];

        matched[0][0] = true;
        for (int j = 1; j <= pattern.length(); j++) {
            if (pattern.charAt(j - 1) == '*')
                matched[0][j] = matched[0][j - 2];
        }
        for (int i = 1; i <= text.length(); i++) {
            char t = text.charAt(i - 1);
            for (int j = 1; j <= pattern.length(); j++) {
                char p = pattern.charAt(j - 1);
                if (p == '*' ) {
                    if (pattern.charAt(j - 2) == t || pattern.charAt(j - 2) == '.') {
                        matched[i][j] = matched[i][j - 1] || matched[i][j - 2] || matched[i - 1][j];
                    } else {
                        matched[i][j] = matched[i][j - 2];
                    }
                } else if ((p == t || p == '.') && matched[i - 1][j - 1]) {
                    matched[i][j] = true;
                }
            }
        }
        return matched[text.length()][pattern.length()];
    }
}
