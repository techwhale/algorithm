package org.practise.algorithm.leetcode.binarysearch.medium;
/**
 * Suppose an array sorted in ascending order is rotated at some pivot unknown to you beforehand.
 *
 * (i.e.,  [0,1,2,4,5,6,7] might become  [4,5,6,7,0,1,2]).
 *
 * Find the minimum element.
 *
 * You may assume no duplicate exists in the array.
 *
 * Example 1:
 *
 * Input: [3,4,5,1,2]
 * Output: 1
 * Example 2:
 *
 * Input: [4,5,6,7,0,1,2]
 * Output: 0
 */
public class FindMinimumInRotatedSortedArray {
    public int findMin(int[] nums) {
        int N = nums.length;
        if (N == 1 || nums[0] < nums[N - 1]){
            return nums[0];
        }

        int left = 0;
        int right = N - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] > nums[mid + 1])
                return nums[mid + 1];

            if (nums[mid - 1] > nums[mid])
                return nums[mid];

            if (nums[0] < nums[mid]) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }
}
