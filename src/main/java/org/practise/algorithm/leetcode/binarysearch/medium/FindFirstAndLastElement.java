package org.practise.algorithm.leetcode.binarysearch.medium;

/**
 * 34. Find First and Last Position of Element in Sorted Array
 * Medium
 * Topics
 * Companies
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
public class FindFirstAndLastElement {
    public int[] searchRange(int[] nums, int target) {
        int left = findOccurrence(nums, target, true);
        if (left < 0 || nums[left] != target) {
            return new int[] {-1, -1};
        }
        return new int[] {left, findOccurrence(nums, target, false)};
    }

    private int findOccurrence(int[] nums, int target, boolean isFirst) {
        int left = 0;
        int right = nums.length - 1;
        while (left < right) {
            int mid = (left + right) / 2;
            if (nums[mid] == target) {
                if (isFirst) {
                    if (mid == left || nums[mid - 1] != nums[mid]) {
                        return mid;
                    }
                    right = mid - 1;
                } else {
                    if (mid == right || nums[mid + 1] != nums[mid]) {
                        return mid;
                    }
                    left = mid + 1;
                }
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return right;
    }
}
