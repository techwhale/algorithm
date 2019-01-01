package org.practise.algorithm.interestingideas;

/**
 * Given an array nums, there is a sliding window of size k which is moving from the very left of the array to the very right.
 * You can only see the k numbers in the window. Each time the sliding window moves right by one position. Return the max sliding window.
 *
 * Example:
 *
 * Input: nums = [1,3,-1,-3,5,3,6,7], and k = 3
 * Output: [3,3,5,5,6,7]
 * Explanation:
 *
 * Window position                Max
 * ---------------               -----
 * [1  3  -1] -3  5  3  6  7       3
 *  1 [3  -1  -3] 5  3  6  7       3
 *  1  3 [-1  -3  5] 3  6  7       5
 *  1  3  -1 [-3  5  3] 6  7       5
 *  1  3  -1  -3 [5  3  6] 7       6
 *  1  3  -1  -3  5 [3  6  7]      7
 * Note:
 * You may assume k is always valid, 1 ≤ k ≤ input array's size for non-empty array.
 *
 * Follow up:
 * Could you solve it in linear time?
 */
public class SlidingWindowMaximum {
    public int[] maxSlidingWindow(int[] nums, int k) {
        if (nums == null || k == 0 || nums.length < 1) return new int[0];
        int N = nums.length;
        int[] max_left = new int[N], max_right = new int[N], result = new int[N - k + 1];
        max_left[0] = nums[0]; max_right[N - 1] = nums[N - 1];

        for (int i = 1, j = N - (i + 1); i < N; i++, j--) {
            max_left[i] = i % k == 0 ? nums[i] : Math.max(nums[i], max_left[i - 1]);
            max_right[j] = j % k == 0 ? nums[j]: Math.max(nums[j], max_right[j + 1]);
        }

        for (int i = 0; i + k <= N; i++) {
            result[i] = Math.max(max_right[i], max_left[i + k -1]);
        }
        return result;
    }
}
