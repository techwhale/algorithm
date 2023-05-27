package org.practise.algorithm.leetcode.array.hard;

/**
 * 42. Trapping Rain Water
 *
 * Given n non-negative integers representing an elevation map where the width of each bar is 1, compute how much water it can trap after raining.
 * Example 1:
 *
 * Input: height = [0,1,0,2,1,0,1,3,2,1,2,1]
 * Output: 6
 * Explanation: The above elevation map (black section) is represented by array [0,1,0,2,1,0,1,3,2,1,2,1]. In this case, 6 units of rain water (blue section) are being trapped.
 * Example 2:
 *
 * Input: height = [4,2,0,3,2,5]
 * Output: 9
 *
 *
 * Constraints:
 *
 * n == height.length
 * 1 <= n <= 2 * 104
 * 0 <= height[i] <= 105
 */
public class TrappingRainWater {
    public int trap(int[] height) {
        int left = 0, right = height.length - 1;
        int leftMax = 0, rightMax = 0;
        int trappedWater = 0;
        while (left < right) {
            if (height[left] < height[right]) {
                leftMax = Math.max(leftMax, height[left]);
                trappedWater += leftMax - height[left];
                left++;
            } else {
                rightMax = Math.max(rightMax, height[right]);
                trappedWater += rightMax - height[right];
                right--;
            }
        }
        return trappedWater;
//        un optimized approach
//        int ans = 0;
//        int size = height.length;
//        for (int i = 1; i < size - 1; i++) {
//            int left_max = 0, right_max = 0;
//            for (int j = i; j >= 0; j--) { //Search the left part for max bar size
//                left_max = Math.max(left_max, height[j]);
//            }
//            for (int j = i; j < size; j++) { //Search the right part for max bar size
//                right_max = Math.max(right_max, height[j]);
//            }
//            ans += Math.min(left_max, right_max) - height[i];
//        }
//        return ans;
    }
}
