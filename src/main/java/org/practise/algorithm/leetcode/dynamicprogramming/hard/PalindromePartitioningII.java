package org.practise.algorithm.leetcode.dynamicprogramming.hard;

import java.util.Arrays;

/**
 * Given a string s, partition s such that every substring of the partition is a palindrome.
 *
 * Return the minimum cuts needed for a palindrome partitioning of s.
 *
 * Example:
 *
 * Input: "aab"
 * Output: 1
 * Explanation: The palindrome partitioning ["aa","b"] could be produced using 1 cut.
 */
public class PalindromePartitioningII {
    public int minCut(String word) {
        int N = word.length();
        boolean[][] palindrome = new boolean[N][N];
        int[] cut = new int[N];
        for (int i = 0; i < N; i++) {
            cut[i] = i;
            for (int j = 0; j <= i; j++) {
                if (word.charAt(i) == word.charAt(j) && (j + 1 > i - 1 || palindrome[j + 1][i - 1])) {
                    cut[i] = Math.min(cut[i], j == 0 ? 0 : cut[j - 1] + 1);
                    palindrome[j][i] = true;
                }
            }
        }
        return cut[N -1];
    }

//    O(N^3) solution

    private Integer[] memoCuts;
    private Boolean[][] memoPalindrome;
    public int minCut2(String s) {
        memoCuts = new Integer[s.length()];
        memoPalindrome = new Boolean[s.length()][s.length()];
        return findMinimumCuts(s, 0, s.length() - 1, s.length() - 1);
    }

    private int findMinimumCuts(String s, int startIdx, int endIdx, int minimumCuts) {
        if (startIdx == endIdx || isPalindrome(s, startIdx, endIdx)) {
            return 0;
        }

        if (memoCuts[startIdx] != null) {
            return memoCuts[startIdx];
        }

        for (int cutIdx = startIdx; cutIdx < endIdx; cutIdx++) {
            if (isPalindrome(s, startIdx, cutIdx)) {
                minimumCuts = Math.min(minimumCuts, 1 + findMinimumCuts(s, cutIdx + 1, endIdx, minimumCuts));
            }
        }
        memoCuts[startIdx] = minimumCuts;
        return memoCuts[startIdx];
    }


    private boolean isPalindrome(String s, int startIdx, int endIdx) {
        if (memoPalindrome[startIdx][endIdx] != null) {
            return memoPalindrome[startIdx][endIdx];
        }
        if (startIdx >= endIdx) {
            return true;
        }
        memoPalindrome[startIdx][endIdx] = ((startIdx >= endIdx) || (s.charAt(startIdx) == s.charAt(endIdx) && isPalindrome(s, startIdx + 1, endIdx - 1)));
        return memoPalindrome[startIdx][endIdx];
    }
}
