package org.practise.algorithm.interviewbit.dynamicprogramming;

import java.util.Arrays;

/**
 * Given an array of integers, find the length of longest subsequence which is first increasing then decreasing.
 *
 * **Example: **
 *
 * For the given array [1 11 2 10 4 5 2 1]
 *
 * Longest subsequence is [1 2 10 4 2 1]
 *
 * Return value 6
 */
public class LengthOfLongestSubsequence {
    public int longestSubsequenceLength(final int[] nums) {
        final int N = nums.length;
        if (N <= 1) return N;
        int[] increasingSequence = new int[N], decreasingSequence = new int[N];

        Arrays.fill(increasingSequence, 1);
        Arrays.fill(decreasingSequence, 1);

        // fill increasing sequence
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j]) {
                    increasingSequence[i] = Math.max(increasingSequence[i], increasingSequence[j] + 1);
                }
            }
        }

        // fill decreasing sequence
        for (int i = N - 1; i >= 0; i--) {
            for (int j = i + 1; j < N; j++) {
                if (nums[i] > nums[j]) {
                    decreasingSequence[i] = Math.max(decreasingSequence[i], decreasingSequence[j] + 1);
                }
            }
        }

        int maxSequence = 0;
        for (int i = 0; i < N; i++) {
            maxSequence = Math.max(maxSequence, increasingSequence[i] + decreasingSequence[i] - 1);
        }

        return maxSequence;
    }
}
