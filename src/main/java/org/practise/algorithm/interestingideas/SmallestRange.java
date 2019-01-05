package org.practise.algorithm.interestingideas;

import java.util.List;
import java.util.PriorityQueue;

/**
 * You have k lists of sorted integers in ascending order. Find the smallest range that includes at least one number from each of the k lists.
 *
 * We define the range [a,b] is smaller than range [c,d] if b-a < d-c or a < c if b-a == d-c.
 *
 * Example 1:
 * Input:[[4,10,15,24,26], [0,9,12,20], [5,18,22,30]]
 * Output: [20,24]
 * Explanation:
 * List 1: [4, 10, 15, 24,26], 24 is in range [20,24].
 * List 2: [0, 9, 12, 20], 20 is in range [20,24].
 * List 3: [5, 18, 22, 30], 22 is in range [20,24].
 * Note:
 * The given list may contain duplicates, so ascending order means >= here.
 * 1 <= k <= 3500
 * -105 <= value of elements <= 105.
 * For Java users, please note that the input type has been changed to List<List<Integer>>. And after you reset the code template, you'll see this point.
 */
public class SmallestRange {
    public int[] smallestRange(List<List<Integer>> nums) {
        int min = 0, max = Integer.MAX_VALUE;
        int[] next = new int[nums.size()];
        PriorityQueue<Integer> minimumQueue = new PriorityQueue<Integer>((i, j) -> (
                nums.get(i).get(next[i]) - nums.get(j).get(next[j])
        ));

        int currentMax = Integer.MIN_VALUE;
        for (int i = 0; i < nums.size(); i++) {
            minimumQueue.offer(i);
            currentMax = Math.max(currentMax, nums.get(i).get(0));
        }

        boolean notReachedEnd = true;
        for (int i = 0; i < nums.size() && notReachedEnd; i++) {
            for (int j = 0; j < nums.get(i).size() && notReachedEnd; j++) {
                int min_index = minimumQueue.poll();
                if (max - min > currentMax - nums.get(min_index).get(next[min_index])) {
                    max = currentMax;
                    min = nums.get(min_index).get(next[min_index]);
                }
                next[min_index]++;
                if (next[min_index] == nums.get(min_index).size()) {
                    notReachedEnd = false;
                    break;
                }

                minimumQueue.offer(min_index);
                currentMax = Math.max(currentMax, nums.get(min_index).get(next[min_index]));
            }
        }
        return new int[] {min, max};
    }
}
