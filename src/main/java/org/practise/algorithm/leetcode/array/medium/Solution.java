package org.practise.algorithm.leetcode.array.medium;

import java.util.Arrays;

public class Solution {
    public int removeDuplicates(int[] nums) {
        if (nums == null || nums.length <= 0) {
            return 0;
        }
        int duplicate_count = 1, length = 1;
        for (int right = 1, left = 1; right < nums.length; right++, left++) {

            if (nums[right] == nums[right - 1]) { duplicate_count++; }
            else duplicate_count = 1;

            if (duplicate_count > 2) {
                while (right < nums.length && nums[right] == nums[right - 1])  {
                    right++;
                }
                duplicate_count = 1;
            }
            if (right < nums.length) {
                nums[left] = nums[right];
                length++;
            }
        }
        return length;
    }
    public static void main(String args[]) {
        int[] nums = new int[] {1, 1, 1 , 2, 2, 2};
        Solution solution = new Solution();
        final int length = solution.removeDuplicates(nums);
        System.out.println(length);
        System.out.println(Arrays.toString(nums));
    }
}
