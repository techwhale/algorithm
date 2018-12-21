package org.practise.algorithm.leetcode.dynamicprogramming.medium;

/**
 * A message containing letters from A-Z is being encoded to numbers using the following mapping:
 *
 * 'A' -> 1
 * 'B' -> 2
 * ...
 * 'Z' -> 26
 * Given a non-empty string containing only digits, determine the total number of ways to decode it.
 *
 * Example 1:
 *
 * Input: "12"
 * Output: 2
 * Explanation: It could be decoded as "AB" (1 2) or "L" (12).
 * Example 2:
 *
 * Input: "226"
 * Output: 3
 * Explanation: It could be decoded as "BZ" (2 26), "VF" (22 6), or "BBF" (2 2 6).
 */
public class DecodeWays {
    int numDecodings(String str) {
        if (str == null || str.isEmpty() || str.startsWith("0")) return 0;
        int p = 1, pp = 1;
        int previousDigit = str.charAt(0) - '0';
        for (int i = 1; i < str.length(); i++) {
            int currentDigit = str.charAt(i) - '0';
            if (currentDigit == 0)  {
                p = 0;
            }
            int value = previousDigit * 10 + currentDigit;
            if (10 <= value && value <= 26) {
                p += pp;
                pp = p - pp;
            } else {
                pp = p;
            }
            previousDigit = currentDigit;
        }
        return p;
    }

}
