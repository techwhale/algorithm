package org.practise.algorithm.leetcode.binarysearch.medium;

/**
 * Given an array of integers nums sorted in non-decreasing order, find the starting and ending position of a given target value.
 *
 * If target is not found in the array, return [-1, -1].
 *
 * You must write an algorithm with O(log n) runtime complexity.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [5,7,7,8,8,10], target = 8
 * Output: [3,4]
 * Example 2:
 *
 * Input: nums = [5,7,7,8,8,10], target = 6
 * Output: [-1,-1]
 * Example 3:
 *
 * Input: nums = [], target = 0
 * Output: [-1,-1]
 *
 *
 * Constraints:
 *
 * 0 <= nums.length <= 105
 * -109 <= nums[i] <= 109
 * nums is a non-decreasing array.
 * -109 <= target <= 109
 */
public class FirstLastPosition {
    public int[] searchRange(int[] nums, int target) {
        int start = findRange(nums, target, true);
        if (start == -1) {
            return new int[]{-1, -1};
        }
        int end = findRange(nums, target, false);
        return new int[] {start, end};
    }


    private int findRange(int[] nums, int target, boolean first) {
        int low = 0, high = nums.length - 1;
        while (low <= high) {
            int mid = (low + high)/ 2;
            if (nums[mid] == target) {
                if (first) {
                    if (mid == low || nums[mid - 1] != target) {
                        return mid;
                    } else {
                        high = mid - 1;
                    }
                } else {
                    if (mid == high || nums[mid + 1] != target) {
                        return mid;
                    } else {
                        low = mid + 1;
                    }
                }
            } else if (nums[mid] > target) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return -1;
    }
}
