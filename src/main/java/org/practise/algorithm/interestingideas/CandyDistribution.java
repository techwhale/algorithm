package org.practise.algorithm.interestingideas;

import java.util.Arrays;

/**
 * There are N children standing in a line. Each child is assigned a rating value.
 *
 * You are giving candies to these children subjected to the following requirements:
 *
 * Each child must have at least one candy.
 * Children with a higher rating get more candies than their neighbors.
 * What is the minimum candies you must give?
 *
 * Example 1:
 *
 * Input: [1,0,2]
 * Output: 5
 * Explanation: You can allocate to the first, second and third child with 2, 1, 2 candies respectively.
 * Example 2:
 *
 * Input: [1,2,2]
 * Output: 4
 * Explanation: You can allocate to the first, second and third child with 1, 2, 1 candies respectively.
 *              The third child gets 1 candy because it satisfies the above two conditions.
 */
public class CandyDistribution {

    public int candy(int[] ratings) {
        int N = ratings.length;
        if (N <= 1) return N;
        int total_candies = 0;
        int[] candies = new int[N];
        Arrays.fill(candies, 1);
        for (int i = 1; i < N; i++) {
            if (ratings[i - 1] < ratings[i]) {
                candies[i] = candies[i - 1] + 1;
            }
        }
        for (int i = N - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                candies[i] = Math.max(candies[i], candies[i + 1] + 1);
            }
        }

        for (int i = 0; i < N; i++) {
            total_candies += candies[i];
        }

        return total_candies;
    }

//    // https://leetcode.com/problems/candy/solution/
//    public int candy(int[] ratings) {
//        int N = ratings.length;
//        if (N <= 1) return N;
//        int up = 0, down = 0, previous_slope = 0, current_slope = 0, candies = 0;
//        for (int i = 1; i < N; i++) {
//            current_slope = ratings[i - 1] < ratings[i] ? 1 : (ratings[i - 1] > ratings[i] ? -1 : 0);
//            if ((previous_slope > 0 && current_slope == 0) || (previous_slope < 0 && current_slope >= 0)) {
//                candies += count(up) + count(down) + Math.max(up, down);
//                up = 0;
//                down = 0;
//            }
//
//            if (current_slope > 0) up++;
//            if (current_slope < 0) down++;
//            if (current_slope == 0) candies += 1;
//
//            previous_slope = current_slope;
//        }
//        candies += count(up) + count(down) + Math.max(up, down) + 1;
//        return candies;
//    }
//
//    private int count(int n) {
//        return (n * (n + 1)/ 2);
//    }
}
