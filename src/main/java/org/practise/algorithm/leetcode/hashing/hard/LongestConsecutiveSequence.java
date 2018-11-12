package org.practise.algorithm.leetcode.hashing.hard;

import java.util.HashSet;
import java.util.Set;

/**
 * Given an unsorted array of integers, find the length of the longest consecutive elements sequence.
 *
 * Your algorithm should run in O(n) complexity.
 *
 * Example:
 *
 * Input: [100, 4, 200, 1, 3, 2]
 * Output: 4
 * Explanation: The longest consecutive elements sequence is [1, 2, 3, 4]. Therefore its length is 4.
 */
public class LongestConsecutiveSequence {
    public int longestConsecutive(int[] nums) {
        int longestStreak = 0;
        Set<Integer> uniqueSet = new HashSet<>();
        int N = nums.length;
        for (int i = 0; i < N; i++) {
            int elem = nums[i];
            uniqueSet.add(elem);
        }

        for (int i = 0; i < N && ! uniqueSet.isEmpty(); i++) {
            int elem = nums[i];
            if (uniqueSet.contains(elem)){
                int currentStreak = getConsecutiveLength(elem, uniqueSet);
                longestStreak = Math.max(longestStreak, currentStreak);
            }
        }
        return longestStreak;
    }

    private int getConsecutiveLength(int value, Set<Integer> uniqueSet) {
        if (! uniqueSet.contains(value))  {
            return 0;
        }
        uniqueSet.remove(value);
        return 1 + getConsecutiveLength(value - 1, uniqueSet) + getConsecutiveLength(value + 1, uniqueSet);
    }
}
