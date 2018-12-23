package org.practise.algorithm.interviewbit.dynamicprogramming;

import java.util.HashMap;
import java.util.Map;

/**
 * Find longest Arithmetic Progression in an integer array and return its length. More formally,
 * find longest sequence of indeces, 0 < i1 < i2 < … < ik < ArraySize(0-indexed) such that sequence A[i1], A[i2], …, A[ik] is an Arithmetic Progression.
 * Arithmetic Progression is a sequence in which all the differences between consecutive pairs are the same,
 * i.e sequence B[0], B[1], B[2], …, B[m - 1] of length m is an Arithmetic Progression if
 * and only if B[1] - B[0] == B[2] - B[1] == B[3] - B[2] == … == B[m - 1] - B[m - 2].
 * Examples
 * 1) 1, 2, 3(All differences are equal to 1)
 * 2) 7, 7, 7(All differences are equal to 0)
 * 3) 8, 5, 2(Yes, difference can be negative too)
 *
 * Samples
 * 1) Input: 3, 6, 9, 12
 * Output: 4
 *
 * 2) Input: 9, 4, 7, 2, 10
 * Output: 3(If we choose elements in positions 1, 2 and 4(0-indexed))
 */
public class LongestArithmeticProgression {
    public int solve(final int[] nums) {
        if (nums.length < 3) return nums.length;
        Map<Integer, Integer>[] progressionCount = new HashMap[nums.length];
        for (int i = 0; i < nums.length; i++) {
            progressionCount[i] = new HashMap<>();
        }

        int maxProgression = 0;
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                int progression = nums[j] - nums[i];
                Map<Integer, Integer> map = progressionCount[i];
                int carry_forward_progression = map.getOrDefault(progression, 1) + 1;
                map = progressionCount[j];
                int max_carry_forward_progression = Math.max(map.getOrDefault(progression, 0), carry_forward_progression);
                map.put(progression, max_carry_forward_progression);
                maxProgression = Math.max(maxProgression, max_carry_forward_progression);
            }
            progressionCount[i] = null; // remove object reference for clean up
        }
        return maxProgression;
    }
}
