package org.practise.algorithm.leetcode.dynamicprogramming.hard;

import java.util.Arrays;

/**
 * 312. Burst Balloons
 *
 * Given n balloons, indexed from 0 to n-1. Each balloon is painted with a number on it represented by array nums.
 * You are asked to burst all the balloons. If the you burst balloon i you will get nums[left] * nums[i] * nums[right] coins.
 * Here left and right are adjacent indices of i. After the burst, the left and right then becomes adjacent.
 *
 * Find the maximum coins you can collect by bursting the balloons wisely.
 *
 * Note:
 *
 * You may imagine nums[-1] = nums[n] = 1. They are not real therefore you can not burst them.
 * 0 ≤ n ≤ 500, 0 ≤ nums[i] ≤ 100
 * Example:
 *
 * Input: [3,1,5,8]
 * Output: 167
 * Explanation: nums = [3,1,5,8] --> [3,5,8] -->   [3,8]   -->  [8]  --> []
 *              coins =  3*1*5      +  3*5*8    +  1*3*8      + 1*8*1   = 167
 */

public class BurstBalloons {
    public int maxCoins(int[] nums) {
        int n = nums.length + 2;
        int[] newNums = new int[n];
        System.arraycopy(nums, 0, newNums, 1, n - 2);
        newNums[0] = newNums[n - 1] = 1; // adjust corner cases

        int[][] memo = new int[n][n];
        for (int i = 0; i < n; i++){
            Arrays.fill(memo[i], -1);
        }

        return dp(newNums, memo, 1, n - 2);
    }

    private int dp(int[] nums, int[][] memo, int left, int right) {
        if (right - left < 0) {
            return 0;
        }

        if (memo[left][right] != -1) {
            return memo[left][right];
        }

        int maxCoins = 0;
        for (int i = left; i <= right; i++) {
            int collected = nums[left - 1] * nums[i] * nums[right + 1];
            int remaining = dp(nums, memo, left, i - 1) + dp(nums, memo, i + 1, right);
            maxCoins = Math.max(maxCoins, collected + remaining);
        }
        memo[left][right] = maxCoins;
        return maxCoins;
    }
}
