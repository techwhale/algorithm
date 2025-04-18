package org.practise.algorithm.leetcode.array.medium;

import java.util.LinkedList;

/**
 * 1004. Max Consecutive Ones III
 *
 * Given a binary array nums and an integer k, return the maximum number of consecutive 1's in the array if you can flip at most k 0's.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,1,1,0,0,0,1,1,1,1,0], k = 2
 * Output: 6
 * Explanation: [1,1,1,0,0,1,1,1,1,1,1]
 * Bolded numbers were flipped from 0 to 1. The longest subarray is underlined.
 * Example 2:
 *
 * Input: nums = [0,0,1,1,0,0,1,1,1,0,1,1,0,0,0,1,1,1,1], k = 3
 * Output: 10
 * Explanation: [0,0,1,1,1,1,1,1,1,1,1,1,0,0,0,1,1,1,1]
 * Bolded numbers were flipped from 0 to 1. The longest subarray is underlined.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 105
 * nums[i] is either 0 or 1.
 * 0 <= k <= nums.length
 */
public class MaxConsecutiveOnesIII {

    // Two pointer approach
    public int longestOnes2(int[] nums, int k) {
        int left = 0, right = 0;
        for (; right < nums.length; right++) {
            if (nums[right] == 0) {
                k--;
            }

            if (k < 0) {
                k += 1 - nums[left];
                left++;
            }
        }
        return right - left;
    }
    public int longestOnes(int[] nums, int k) {
        LinkedList<Integer> zeroPositions = new LinkedList<>();
        int maxConsecutive = 0;
        int maxConsecutiveSoFar = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == 0) {
                if (zeroPositions.size() < k) {
                    zeroPositions.offerLast(i);
                    maxConsecutiveSoFar++;
                } else {
                    int firstZeroIndex = zeroPositions.pollFirst();
                    maxConsecutiveSoFar = i - firstZeroIndex - 1;
                    i--;
                    continue;
                }
            } else {
                maxConsecutiveSoFar++;
            }
            maxConsecutive = Math.max(maxConsecutive, maxConsecutiveSoFar);
        }
        return maxConsecutive;
    }
}
