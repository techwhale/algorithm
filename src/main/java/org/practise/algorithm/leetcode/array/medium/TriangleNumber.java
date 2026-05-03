package org.practise.algorithm.leetcode.array.medium;

import java.util.Arrays;

/**
 * 611. Valid Triangle Number
 * Solved
 * Medium
 * Topics
 * Companies
 * Given an integer array nums, return the number of triplets chosen from the array that can make triangles if we take them as side lengths of a triangle.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [2,2,3,4]
 * Output: 3
 * Explanation: Valid combinations are:
 * 2,3,4 (using the first 2)
 * 2,3,4 (using the second 2)
 * 2,2,3
 * Example 2:
 *
 * Input: nums = [4,2,3,4]
 * Output: 4
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 1000
 * 0 <= nums[i] <= 1000
 *
 */
public class TriangleNumber {
    public int triangleNumber(int[] nums) {
        int N = nums.length;
        Arrays.sort(nums);
        int count = 0;
        for (int i = 0; i + 2 < N; i++) {
            int k = i + 2;
            for (int j = i + 1; j + 1 < N; j++) {
                if (k <= j) {
                    k = j + 1;
                }
                while (k < N && nums[i] + nums[j] > nums[k]) {
                    k++;
                }
                count += (k - j - 1);
            }
        }
        return count;
    }
}
