package org.practise.algorithm.leetcode.interestingsolution;

/**
 *
 * 689. Maximum Sum of 3 Non-Overlapping Subarrays
 *
 * In a given array nums of positive integers, find three non-overlapping subarrays with maximum sum.
 *
 * Each subarray will be of size k, and we want to maximize the sum of all 3*k entries.
 *
 * Return the result as a list of indices representing the starting position of each interval (0-indexed).
 * If there are multiple answers, return the lexicographically smallest one.
 *
 * Example:
 * Input: [1,2,1,2,6,7,5,1], 2
 * Output: [0, 3, 5]
 * Explanation: Subarrays [1, 2], [2, 6], [7, 5] correspond to the starting indices [0, 3, 5].
 * We could have also taken [2, 1], but an answer of [1, 3, 5] would be lexicographically larger.
 * Note:
 * nums.length will be between 1 and 20000.
 * nums[i] will be between 1 and 65535.
 * k will be between 1 and floor(nums.length / 3).
 */
public class MaximumSumOf3NonOverlappingSubarrays {
    public int[] maxSumOfThreeSubarrays(int[] nums, int k) {
        int[] w = new int[nums.length - k + 1];
        int sum = 0;
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            if (i >= k) sum -= nums[i - k];
            if (i >= k - 1) w[i - k + 1] = sum;
        }

        int[] left = new int[w.length], right = new int[w.length];
        int max_left = 0, max_right = w.length - 1;

        for (int leftIndex = 0, rightIndex = w.length - 1; leftIndex < w.length && rightIndex >= 0; leftIndex++, rightIndex--) {
            if (w[leftIndex] > w[max_left]) max_left = leftIndex;
            if (w[rightIndex] > w[max_right]) max_right = rightIndex;
            left[leftIndex] = max_left;
            right[rightIndex] = max_right;
        }

        int[] result = new int[] {-1, -1, -1};
        for (int j = k; j + k < w.length; j++) {
            int a = left[j - k], b = j, c = right[j + k];
            if (result[0] == -1 || (w[a] + w[b] + w[c]) > (w[result[0]] + w[result[1]] + w[result[2]])) {
                result[0] = a;
                result[1] = b;
                result[2] = c;
            }
        }
        return result;
    }
}
