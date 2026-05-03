package org.practise.algorithm.leetcode.array.medium;

import java.util.Random;

/**
 * 384. Shuffle an Array
 *
 * Given an integer array nums, design an algorithm to randomly shuffle the array. All permutations of the array should be equally likely as a result of the shuffling.
 *
 * Implement the Solution class:
 *
 * Solution(int[] nums) Initializes the object with the integer array nums.
 * int[] reset() Resets the array to its original configuration and returns it.
 * int[] shuffle() Returns a random shuffling of the array.
 *
 *
 * Example 1:
 *
 * Input
 * ["Solution", "shuffle", "reset", "shuffle"]
 * [[[1, 2, 3]], [], [], []]
 * Output
 * [null, [3, 1, 2], [1, 2, 3], [1, 3, 2]]
 *
 * Explanation
 * Solution solution = new Solution([1, 2, 3]);
 * solution.shuffle();    // Shuffle the array [1,2,3] and return its result.
 *                        // Any permutation of [1,2,3] must be equally likely to be returned.
 *                        // Example: return [3, 1, 2]
 * solution.reset();      // Resets the array back to its original configuration [1,2,3]. Return [1, 2, 3]
 * solution.shuffle();    // Returns the random shuffling of array [1,2,3]. Example: return [1, 3, 2]
 *
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 50
 * -106 <= nums[i] <= 106
 * All the elements of nums are unique.
 * At most 104 calls in total will be made to reset and shuffle.
 */
public class ShuffleArray {
    private int[] source;
    private int[] nums;
    private Random rand;
    public ShuffleArray(int[] nums) {
        this.source = nums;
        this.nums = source.clone();
        this.rand = new Random();
    }

    public int[] reset() {
        nums = source;
        source = source.clone();
        return nums;
    }

    public int[] shuffle() {
        for (int i = 0; i < nums.length; i++) {
            swap(nums, i, findRandomPosition(i, nums.length));
        }
        return nums;
    }

    private int findRandomPosition(int min, int max) {
        return rand.nextInt(max - min) + min;
    }

    private void swap(int[] values, int i, int j) {
        int temp = values[i];
        values[i] = values[j];
        values[j] = temp;
    }
}
