package org.practise.algorithm.leetcode.array.medium;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * 215. Kth Largest Element in an Array
 *
 * Given an integer array nums and an integer k, return the kth largest element in the array.
 *
 * Note that it is the kth largest element in the sorted order, not the kth distinct element.
 *
 * Can you solve it without sorting?
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [3,2,1,5,6,4], k = 2
 * Output: 5
 * Example 2:
 *
 * Input: nums = [3,2,3,1,2,4,5,5,6], k = 4
 * Output: 4
 *
 *
 * Constraints:
 *
 * 1 <= k <= nums.length <= 105
 * -104 <= nums[i] <= 104
 */
public class FindKthLargest {

    public int findKthLargest2(int[] nums, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> {
            return (a == b) ? 0 : ((a < b) ? -1: 1);
        });
        for (int num : nums) {
            pq.offer(num);
            if (pq.size() > k) {
                pq.poll();
            }
        }
        return pq.poll();
    }

    public int findKthLargest(int[] nums, int k) {
        List<Integer> values = new ArrayList<>();
        for (int num : nums) {
            values.add(num);
        }
        return quickSelect(values, k);
    }

    private int quickSelect(List<Integer> values, int K) {
        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();
        List<Integer> middle = new ArrayList<>();

        int pivotIndex = new Random().nextInt(values.size());
        int pivotValue = values.get(pivotIndex);
        for (int val : values) {
            if (val > pivotValue) {
                left.add(val);
            } else if (val < pivotValue) {
                right.add(val);
            } else {
                middle.add(val);
            }
        }

        if (K <= left.size()) {
            return quickSelect(left, K);
        }
        if (K > left.size() + middle.size()) {
            return quickSelect(right, K - (left.size() + middle.size()));
        }
        return pivotValue;
    }
}
