package org.practise.algorithm.leetcode.binarysearch.medium;

import java.util.Arrays;
import java.util.Random;

/**
 * Given an array w of positive integers, where w[i] describes the weight of index i, write a function pickIndex which randomly picks an index in proportion to its weight.
 *
 * Note:
 *
 * 1 <= w.length <= 10000
 * 1 <= w[i] <= 10^5
 * pickIndex will be called at most 10000 times.
 * Example 1:
 *
 * Input:
 * ["Solution","pickIndex"]
 * [[[1]],[]]
 * Output: [null,0]
 * Example 2:
 *
 * Input:
 * ["Solution","pickIndex","pickIndex","pickIndex","pickIndex","pickIndex"]
 * [[[1,3]],[],[],[],[],[]]
 * Output: [null,0,1,1,1,0]
 * Explanation of Input Syntax:
 *
 * The input is two lists: the subroutines called and their arguments.
 * Solution's constructor has one argument, the array w. pickIndex has no arguments. Arguments are always wrapped with a list, even if there aren't any.
 */
public class RandomPickWithWeight {
    Random random;
    int[] weightedSum;

    public RandomPickWithWeight(int[] w) {
        random = new Random();
        weightedSum = new int[w.length];
        for (int i = 0; i < weightedSum.length; i++) {
            weightedSum[i] = w[i] + ((i == 0) ? 0 : weightedSum[i - 1]);
        }
    }

    public int pickIndex() {
        int low = 0, high = weightedSum.length - 1, target = random.nextInt(weightedSum[high]) + 1;
        int position = Arrays.binarySearch(weightedSum, target);
//        while (low != high) {
//            int mid = (low + high) / 2;
//            if (target >= weightedSum[mid]) low = mid + 1;
//            else high = mid;
//        }
        if (position < 0) {
            position = - position - 1;
        }
        return position;
    }
}
