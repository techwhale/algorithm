package org.practise.algorithm.leetcode.binarysearch.hard;

/**
 * 4. Median of Two Sorted Arrays
 * Hard
 * Given two sorted arrays nums1 and nums2 of size m and n respectively, return the median of the two sorted arrays.
 *
 * The overall run time complexity should be O(log (m+n)).
 *
 * Example 1:
 *
 * Input: nums1 = [1,3], nums2 = [2]
 * Output: 2.00000
 * Explanation: merged array = [1,2,3] and median is 2.
 * Example 2:
 *
 * Input: nums1 = [1,2], nums2 = [3,4]
 * Output: 2.50000
 * Explanation: merged array = [1,2,3,4] and median is (2 + 3) / 2 = 2.5.
 *
 * Constraints:
 *
 * nums1.length == m
 * nums2.length == n
 * 0 <= m <= 1000
 * 0 <= n <= 1000
 * 1 <= m + n <= 2000
 * -106 <= nums1[i], nums2[i] <= 106
 */

public class MedianOfTwoSortedArray {
    // DO NOT MODIFY BOTH THE LISTS
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        if (nums1.length > nums2.length) {
            return findMedianSortedArrays(nums2, nums1);
        }
        int x = nums1.length, y = nums2.length;
        int low = 0, high = x;

        while (low <= high) {
            int partitionX = (low + high)/2;
            int partitionY = (x + y + 1)/2 - partitionX;

            int leftX = partitionX == 0? Integer.MIN_VALUE : nums1[partitionX - 1];
            int leftY = partitionY == 0? Integer.MIN_VALUE : nums2[partitionY - 1];

            int rightX = partitionX == x? Integer.MAX_VALUE : nums1[partitionX];
            int rightY = partitionY == y? Integer.MAX_VALUE : nums2[partitionY];

            if (leftX <= rightY && leftY <= rightX) {
                if ((x+y)%2 == 0) {
                    return ((double) Math.max(leftX, leftY) + Math.min(rightX,rightY))/2;
                } else {
                    return (double) Math.max(leftX, leftY);
                }
            } else if (leftX > rightY) {
                high = partitionX - 1;
            } else {
                low = partitionX + 1;
            }
        }
        return 0; // never reaches
    }
}
