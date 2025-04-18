package org.practise.algorithm.leetcode.dynamicprogramming.hard;

import java.util.HashMap;
import java.util.Map;

public class WildcardMatching {
    /**
     * 44. Wildcard Matching
     * Solved
     * Hard
     * Topics
     * Companies
     * Given an input string (s) and a pattern (p), implement wildcard pattern matching with support for '?' and '*' where:
     *
     * '?' Matches any single character.
     * '*' Matches any sequence of characters (including the empty sequence).
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
     * Input: s = "aa", p = "*"
     * Output: true
     * Explanation: '*' matches any sequence.
     * Example 3:
     *
     * Input: s = "cb", p = "?a"
     * Output: false
     * Explanation: '?' matches 'c', but the second letter is 'a', which does not match 'b'.
     *
     *
     * Constraints:
     *
     * 0 <= s.length, p.length <= 2000
     * s contains only lowercase English letters.
     * p contains only lowercase English letters, '?' or '*'
     */


    public boolean isMatch2(String s, String p) {
        int sIdx = 0, pIdx = 0, sLen = s.length(), pLen = p.length();
        int starIdx = -1, sTmpIdx = -1;
        while (sIdx < sLen) {
            if (pIdx < pLen && (s.charAt(sIdx) == p.charAt(pIdx) || p.charAt(pIdx) == '?')) {
                sIdx++;
                pIdx++;
            } else if (pIdx < pLen && p.charAt(pIdx) == '*') {
                // no match condition
                starIdx = pIdx;
                sTmpIdx = sIdx;
                pIdx++;
            } else if (starIdx == -1) {
                return false;
            } else {
                pIdx = starIdx + 1;
                sIdx = sTmpIdx + 1;
                sTmpIdx = sIdx;
            }
        }
        // The remaining characters in the pattern should all be '*' characters
        for (int i = pIdx; i < pLen; i++) {
            if (p.charAt(i) != '*') {
                return false;
            }
        }
        return true;
    }

    public boolean isMatch(String s, String p) {
        Map<String, Boolean> map = new HashMap<>();
        String newPattern = removeDuplicates(p);
        return isMatch(0, 0, s, newPattern, map);
    }

    private boolean isMatch(int i, int j, String s, String p, Map<String, Boolean> map) {
        String key = i +"-" +j;
        if (map.containsKey(key)) {
            return map.get(key);
        }
        boolean result = false;
        if (j == p.length()) {
            result = i == s.length();
        } else if (i == s.length()) {
            result = (j + 1 == p.length() && p.charAt(j) == '*');
        } else if (s.charAt(i) == p.charAt(j) || p.charAt(j) == '?') {
            result = isMatch(i + 1, j + 1, s, p, map);
        } else if (p.charAt(j) == '*') {
            result = isMatch(i, j + 1, s, p, map) || isMatch(i + 1, j, s, p, map);
        }
        map.put(key, result);
        return result;
    }

    private String removeDuplicates(String pattern) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < pattern.length(); i++) {
            if (i != 0 && pattern.charAt(i) == '*' && pattern.charAt(i - 1) == pattern.charAt(i)) {
                continue;
            }
            builder.append(pattern.charAt(i));
        }
        return builder.toString();
    }
}
