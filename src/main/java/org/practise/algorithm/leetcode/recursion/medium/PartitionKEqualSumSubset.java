package org.practise.algorithm.leetcode.recursion.medium;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 698. Partition to K Equal Sum Subsets
 * Solved
 *
 * Given an integer array nums and an integer k, return true if it is possible to divide this array into k non-empty subsets whose sums are all equal.
 *
 * Example 1:
 * Input: nums = [4,3,2,3,5,2,1], k = 4
 * Output: true
 * Explanation: It is possible to divide it into 4 subsets (5), (1, 4), (2,3), (2,3) with equal sums.
 * Example 2:
 *
 * Input: nums = [1,2,3,4], k = 3
 * Output: false
 *
 *
 * Constraints:
 *
 * 1 <= k <= nums.length <= 16
 * 1 <= nums[i] <= 104
 * The frequency of each element is in the range [1, 4].
 */
public class PartitionKEqualSumSubset {
    public boolean canPartitionKSubsets(int[] nums, int k) {
        int N = nums.length;

        int sum = 0;
        for (int val : nums) {
            sum += val;
        }
        if (sum % k != 0) {
            return false;
        }
        int target = sum / k;
        Arrays.sort(nums);
        reverse(nums);

        char[] visited = new char[N];
        Arrays.fill(visited, '0');
        Map<String, Boolean> memo = new HashMap<>();
        return recurse(nums, 0, 0, 0, k, target, visited, memo);
    }

    private void reverse(int[] nums) {
        for (int i = 0, j = nums.length - 1; i < nums.length; i++, j--) {
            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
        }
    }

    private boolean recurse(int[] nums, int index, int count, int currSum, int k, int target, char[] visited, Map<String, Boolean> memo) {
        int N = nums.length;

        if (count == k - 1) {
            return true;
        }

        if (currSum > target) {
            return false;
        }
        String key = new String(visited);
        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        if (currSum == target) {
            boolean ans = recurse(nums, 0, count + 1, 0, k, target, visited, memo);
            memo.put(key, ans);
            return ans;
        }

        for (int i = 0; i < N; i++) {
            if (visited[i] == '0') {
                visited[i] = '1';

                if (recurse(nums, i + 1, count, currSum + nums[i], k, target, visited, memo)) {
                    return true;

                }
                visited[i] = '0';
            }
        }
        memo.put(key, false);
        return false;
    }
}
