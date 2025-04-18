package org.practise.algorithm.leetcode.array.hard;

import java.util.*;

/**
 * 239. Sliding Window Maximum
 * Hard
 * Topics
 * Companies
 * Hint
 * You are given an array of integers nums, there is a sliding window of size k which is moving from the very left of the array to the very right. You can only see the k numbers in the window. Each time the sliding window moves right by one position.
 *
 * Return the max sliding window.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,3,-1,-3,5,3,6,7], k = 3
 * Output: [3,3,5,5,6,7]
 * Explanation:
 * Window position                Max
 * ---------------               -----
 * [1  3  -1] -3  5  3  6  7       3
 *  1 [3  -1  -3] 5  3  6  7       3
 *  1  3 [-1  -3  5] 3  6  7       5
 *  1  3  -1 [-3  5  3] 6  7       5
 *  1  3  -1  -3 [5  3  6] 7       6
 *  1  3  -1  -3  5 [3  6  7]      7
 * Example 2:
 *
 * Input: nums = [1], k = 1
 * Output: [1]
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 105
 * -104 <= nums[i] <= 104
 * 1 <= k <= nums.length
 */
public class SlidingWindowMaximum {

    public int[] maxSlidingWindow(int[] nums, int k) {
        List<Integer> result = new ArrayList<>();
        Deque<Integer> dq = new ArrayDeque<>();
        for (int i = 0; i < k; i++) {
            while (! dq.isEmpty() && nums[i] >= nums[dq.peekLast()]) {
                dq.pollLast();
            }
            dq.offerLast(i);
        }
        result.add(nums[dq.peekFirst()]);
        for (int i = k; i < nums.length; i++) {
            if (dq.peekFirst() == i - k) {
                dq.pollFirst();
            }
            while (! dq.isEmpty() && nums[i] >= nums[dq.peekLast()]) {
                dq.pollLast();
            }
            dq.offerLast(i);
            result.add(nums[dq.peekFirst()]);

        }
        return result.stream().mapToInt(i -> i).toArray();
    }
//
//    public int[] maxSlidingWindow(int[] nums, int k) {
//        Map<Integer, Integer> count = new HashMap<>();
//        PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> Integer.compare(b, a));
//        List<Integer> result = new ArrayList<>();
//        for (int i = 1; i <= nums.length; i++) {
//            int v = nums[i - 1];
//            count.put(v, count.getOrDefault(v, 0) + 1);
//            pq.offer(v);
//            if (i < k) { // continue add more elements till reach K size
//                continue;
//            }
//            if (i > k) { // start removing elements
//                int removeVal = nums[i - 1 - k];
//                int newCount = count.remove(removeVal) - 1;
//                if (newCount > 0) {
//                    count.put(removeVal, newCount);
//                }
//                while (! count.containsKey(pq.peek())) {
//                    pq.poll();
//                }
//            }
//            result.add(pq.peek());
//        }
//        return result.stream().mapToInt(i -> i).toArray();
//    }
}
