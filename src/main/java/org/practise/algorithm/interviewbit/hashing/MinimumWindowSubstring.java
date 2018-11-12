package org.practise.algorithm.interviewbit.hashing;

import java.util.HashMap;
import java.util.Map;

/**
 * Given a string S and a string T, find the minimum window in S which will contain all the characters in T in linear time complexity.
 * Note that when the count of a character C in T is N, then the count of C in minimum window in S should be at least N.
 *
 * Example :
 *
 * S = "ADOBECODEBANC"
 * T = "ABC"
 * Minimum window is "BANC"
 *
 *  Note:
 * If there is no such window in S that covers all characters in T, return the empty string ''.
 * If there are multiple such windows, return the first occurring minimum window ( with minimum start index ).
 */
public class MinimumWindowSubstring {
    private static final String EMPTY_STRING = "";
    public String minWindow(String S, String T) {
        if (S == null || T == null || S.isEmpty() || T.isEmpty() || S.length() < T.length()) {
            return EMPTY_STRING;
        }

        Map<Character, Integer> dictT = new HashMap<>();
        for (char ch : T.toCharArray()) {
            dictT.put(ch, dictT.getOrDefault(ch, 0) + 1);
        }
        int required = dictT.size();
        int l = 0, r = 0, formed = 0;

        // store minimumLength, left index and right index
        int[] minimumWindow = new int[] {-1, 0, 0};
        Map<Character, Integer> mapSCharCount = new HashMap<>();
        while (r < S.length()) {
            char ch = S.charAt(r);
            mapSCharCount.put(ch, mapSCharCount.getOrDefault(ch, 0) + 1);

            if (dictT.containsKey(ch) && mapSCharCount.get(ch).intValue() == dictT.get(ch).intValue()) {
                formed++;
            }

            while (l <= r && required == formed) {
                if (minimumWindow[0] == -1 || minimumWindow[0] > r - l + 1) {
                    updateMinimumWindow(l, r, minimumWindow);
                }
                ch = S.charAt(l);
                mapSCharCount.put(ch, mapSCharCount.getOrDefault(ch, 0) - 1);
                if (dictT.containsKey(ch) && mapSCharCount.get(ch).intValue() < dictT.get(ch).intValue()) {
                    formed--;
                }
                l++;
            }
            r++;
        }
        return minimumWindow[0] == -1 ? EMPTY_STRING : S.substring(minimumWindow[1], minimumWindow[2] + 1);
    }

    private void updateMinimumWindow(int l, int r, int[] minimumWindow) {
        minimumWindow[0] = r - l + 1;
        minimumWindow[1] = l;
        minimumWindow[2] = r;
    }
}
