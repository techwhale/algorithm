package org.practise.algorithm.leetcode.string.hard;

import java.util.HashMap;
import java.util.Map;

/**
 * 76. Minimum Window Substring
 * Hard
 * Topics
 * Companies
 * Hint
 * Given two strings s and t of lengths m and n respectively, return the minimum window
 * substring
 * of s such that every character in t (including duplicates) is included in the window. If there is no such substring, return the empty string "".
 * <p>
 * The testcases will be generated such that the answer is unique.
 * <p>
 * <p>
 * <p>
 * Example 1:
 * <p>
 * Input: s = "ADOBECODEBANC", t = "ABC"
 * Output: "BANC"
 * Explanation: The minimum window substring "BANC" includes 'A', 'B', and 'C' from string t.
 * Example 2:
 * <p>
 * Input: s = "a", t = "a"
 * Output: "a"
 * Explanation: The entire string s is the minimum window.
 * Example 3:
 * <p>
 * Input: s = "a", t = "aa"
 * Output: ""
 * Explanation: Both 'a's from t must be included in the window.
 * Since the largest window of s only has one 'a', return empty string.
 * <p>
 * <p>
 * Constraints:
 * <p>
 * m == s.length
 * n == t.length
 * 1 <= m, n <= 105
 * s and t consist of uppercase and lowercase English letters.
 * <p>
 * <p>
 * Follow up: Could you find an algorithm that runs in O(m + n) time?
 */
public class MinimumWindowSubstring {
    public String minWindow(String t, String s) {
        Map<Character, Integer> sCharCount = new HashMap<>();
        Map<Character, Integer> tCharCount = new HashMap<>();
        for (char c : s.toCharArray()) {
            sCharCount.put(c, sCharCount.getOrDefault(c, 0) + 1);
        }
        int totalMatchedCharacters = 0;
        int[] resultPosition = {-1, -1};
        int startIdx = 0, endIdx = 0;
        while (endIdx < t.length()) {
            char c = t.charAt(endIdx);
            tCharCount.put(c, tCharCount.getOrDefault(c, 0) + 1);
            if (sCharCount.containsKey(c) && tCharCount.get(c).intValue() == sCharCount.get(c).intValue()) {
                totalMatchedCharacters++;
            }
            while (startIdx <= endIdx && totalMatchedCharacters == sCharCount.size()) {
                if (resultPosition[0] == -1 || ((resultPosition[1] - resultPosition[0]) > (endIdx + 1 - startIdx))) {
                    resultPosition[0] = startIdx;
                    resultPosition[1] = endIdx + 1;
                }
                char deleteChar = t.charAt(startIdx);
                tCharCount.put(deleteChar, tCharCount.getOrDefault(deleteChar, 0) - 1);
                startIdx++;
                if (sCharCount.containsKey(deleteChar) && tCharCount.get(deleteChar).intValue() < sCharCount.get(deleteChar).intValue()) {
                    totalMatchedCharacters--;
                }
            }
            endIdx++;
        }
        return resultPosition[0] == -1 ? "" : t.substring(resultPosition[0], resultPosition[1]);
    }
}
