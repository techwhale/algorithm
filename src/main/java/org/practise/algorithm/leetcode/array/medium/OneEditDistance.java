package org.practise.algorithm.leetcode.array.medium;

/**
 * Given two strings s and t, determine if they are both one edit distance apart.
 *
 * Note:
 *
 * There are 3 possiblities to satisify one edit distance apart:
 *
 * Insert a character into s to get t
 * Delete a character from s to get t
 * Replace a character of s to get t
 * Example 1:
 *
 * Input: s = "ab", t = "acb"
 * Output: true
 * Explanation: We can insert 'c' into s to get t.
 * Example 2:
 *
 * Input: s = "cab", t = "ad"
 * Output: false
 * Explanation: We cannot get t from s by only one step.
 * Example 3:
 *
 * Input: s = "1203", t = "1213"
 * Output: true
 * Explanation: We can replace '0' with '1' to get t.
 */
public class OneEditDistance {
    public boolean isOneEditDistance(String s, String t) {
        if (s == null || t == null || s.equals(t) || Math.abs(s.length() - t.length()) > 1) {
            return false;
        }

        boolean isEdited = false;
        int sIndex = 0, tIndex = 0, sLength = s.length(), tLength = t.length();
        while (sIndex < sLength && tIndex < tLength) {
            if (s.charAt(sIndex) == t.charAt(tIndex)) {
                sIndex++;
                tIndex++;
                continue;
            } else if (isEdited) {
                return false;
            } else if (sLength < tLength) {
                tIndex++;
            } else if (sLength > tLength) {
                sIndex++;
            } else {
                sIndex++;
                tIndex++;
            }
            isEdited = true;
        }
        return true;
    }
}
