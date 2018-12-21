package org.practise.algorithm.leetcode.dynamicprogramming.medium;

import java.util.Arrays;

public class NumberOfLongestIncreasingSubsequence {
    public int findNumberOfLIS(int[] nums) {
        int N = nums.length;
        if (N <= 1) return N;
        int[] lengths = new int[N], counts = new int[N];
        Arrays.fill(counts, 1);

        int maxLength = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j]) {
                  if (lengths[j] >= lengths[i]) {
                      lengths[i] = lengths[j] + 1;
                      counts[i] = counts[j];
                      maxLength = Math.max(maxLength, lengths[i]);
                  } else if (lengths[j] + 1 == lengths[i]) {
                      counts[i] += counts[j];
                  }
                }
            }
        }

        int countMaxLengthSequence = 0;
        for (int i = 0; i < N; i++) {
            if (lengths[i] == maxLength) {
                countMaxLengthSequence += counts[i];
            }
        }

        return countMaxLengthSequence;
    }
}
