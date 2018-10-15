package org.practise.algorithm.leetcode.array.medium;

/**
 * Given an array nums of n integers where n > 1,  return an array output such that output[i] is equal to the product of all the elements of nums except nums[i].
 *
 * Example:
 *
 * Input:  [1,2,3,4]
 * Output: [24,12,8,6]
 * Note: Please solve it without division and in O(n).
 *
 * Follow up:
 * Could you solve it with constant space complexity? (The output array does not count as extra space for the purpose of space complexity analysis.)
 */
public class ProductOfArrayExceptSelf {
    public int[] productExceptSelf(int[] nums) {
        int N = nums.length;
        int[] leftToRightProduct = new int[N + 2], rightToLeftProduct = new int[N + 2];
        leftToRightProduct[0] = rightToLeftProduct[0] = leftToRightProduct[leftToRightProduct.length - 1] = rightToLeftProduct[rightToLeftProduct.length - 1] = 1;
        for (int i = 0; i < N; i++) {
            leftToRightProduct[i + 1] = leftToRightProduct[i] * nums[i];
            rightToLeftProduct[N - i] = rightToLeftProduct[N - i + 1] * nums[N - i - 1];
        }
        int[] result = new int[N];
        for (int i = 0; i < N; i++) {
            result[i] = leftToRightProduct[i] * rightToLeftProduct[i + 2];
        }
        return result;
    }
}
