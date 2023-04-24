package org.practise.algorithm.leetcode.interestingsolution;
/**
 * 307. Range Sum Query - Mutable
 * Medium
 *
 * Given an integer array nums, handle multiple queries of the following types:
 *
 * Update the value of an element in nums.
 * Calculate the sum of the elements of nums between indices left and right inclusive where left <= right.
 * Implement the NumArray class:
 *
 * NumArray(int[] nums) Initializes the object with the integer array nums.
 * void update(int index, int val) Updates the value of nums[index] to be val.
 * int sumRange(int left, int right) Returns the sum of the elements of nums between indices left and right inclusive (i.e. nums[left] + nums[left + 1] + ... + nums[right]).
 *
 * Example 1:
 *
 * Input
 * ["NumArray", "sumRange", "update", "sumRange"]
 * [[[1, 3, 5]], [0, 2], [1, 2], [0, 2]]
 * Output
 * [null, 9, null, 8]
 *
 * Explanation
 * NumArray numArray = new NumArray([1, 3, 5]);
 * numArray.sumRange(0, 2); // return 1 + 3 + 5 = 9
 * numArray.update(1, 2);   // nums = [1, 2, 5]
 * numArray.sumRange(0, 2); // return 1 + 2 + 5 = 8
 *
 * Constraints:
 *
 * 1 <= nums.length <= 3 * 104
 * -100 <= nums[i] <= 100
 * 0 <= index < nums.length
 * -100 <= val <= 100
 * 0 <= left <= right < nums.length
 * At most 3 * 104 calls will be made to update and sumRange.
 */
public class RangeSumQuery1D {
    int[] bits;
    int[] nums;
    int N;
    public RangeSumQuery1D(int[] nums) {
        N = nums.length;
        bits = new int[nums.length + 1];
        this.nums = nums;
        for (int i = 0; i < N; i++){
            updateBIT(i, nums[i]);
        }
    }

    public void update(int index, int val) {
        int newValue = val - nums[index];
        nums[index] = val;
        updateBIT(index, newValue);
    }

    public int sumRange(int left, int right) {
        return querySum(right) - querySum(left - 1);
    }


    void updateBIT(int position, int val) {
        position++;
        for (int i = position; i <= N; i += (i & -i)) {
            bits[i] += val;
        }
    }


    int querySum(int position) {
        position++;
        int sum = 0;
        while (position > 0){
            sum += bits[position];
            position -= (position & - position);
        }
        return sum;
    }
}
