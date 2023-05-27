package org.practise.algorithm.leetcode.string.hard;

/**
 * 10. Regular Expression Matching
 * Hard
 * Given an input string s and a pattern p, implement regular expression matching with support for '.' and '*' where:
 * '.' Matches any single character.
 * '*' Matches zero or more of the preceding element.
 * The matching should cover the entire input string (not partial).
 *
 * Example 1:
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
 * Constraints:
 * 1 <= s.length <= 20
 * 1 <= p.length <= 20
 * s contains only lowercase English letters.
 * p contains only lowercase English letters, '.', and '*'.
 * It is guaranteed for each appearance of the character '*', there will be a previous valid character to match.
 */
public class RegexPatternMatcher {

    enum Result {
        TRUE, FALSE
    }
    Result[][] memo;


//    public boolean isMatch(String text, String pattern) {
//        memo = new Result[text.length() + 1][pattern.length() + 1];
//        return dp(0, 0, text, pattern);
////        if (pattern.isEmpty()) return text.isEmpty();
////        boolean first_match = (!text.isEmpty() &&
////                (pattern.charAt(0) == text.charAt(0) || pattern.charAt(0) == '.'));
////
////        if (pattern.length() >= 2 && pattern.charAt(1) == '*'){
////            return (isMatch(text, pattern.substring(2)) ||
////                    (first_match && isMatch(text.substring(1), pattern)));
////        } else {
////            return first_match && isMatch(text.substring(1), pattern.substring(1));
////        }
//    }

    public boolean isMatch(String text, String pattern) {
        memo = new Result[text.length() + 1][pattern.length() + 1];
        return dp(0, 0, text, pattern);
    }

    private boolean dp(int i , int j, String text, String pattern) {
        if (memo[i][j] != null){
            return memo[i][j] == Result.TRUE;
        }
        boolean ans;
        if (j == pattern.length()) {
            ans = (i == text.length());
        } else {
            boolean first_matched = i < text.length() && (text.charAt(i) == pattern.charAt(j) || pattern.charAt(j) == '.');
            if (j <= pattern.length() - 2 && pattern.charAt(j + 1) == '*') {
                ans = (first_matched && dp(i + 1, j, text, pattern)) || dp(i, j + 2, text, pattern);
            } else {
                ans = first_matched && dp(i + 1, j + 1, text, pattern);
            }
        }
        memo[i][j] = ans? Result.TRUE : Result.FALSE;
        return ans;
    }
}
