package org.practise.algorithm.leetcode.dynamicprogramming.hard;

import java.util.Arrays;

/**
 * 265. Paint House II
 *
 * There are a row of n houses, each house can be painted with one of the k colors. The cost of painting each house with a certain color is different. You have to paint all the houses such that no two adjacent houses have the same color.
 *
 * The cost of painting each house with a certain color is represented by a n x k cost matrix. For example, costs[0][0] is the cost of painting house 0 with color 0; costs[1][2] is the cost of painting house 1 with color 2, and so on... Find the minimum cost to paint all houses.
 *
 * Note:
 * All costs are positive integers.
 *
 * Example:
 *
 * Input: [[1,5,3],[2,9,4]]
 * Output: 5
 * Explanation: Paint house 0 into color 0, paint house 1 into color 2. Minimum cost: 1 + 4 = 5;
 *              Or paint house 0 into color 2, paint house 1 into color 0. Minimum cost: 3 + 2 = 5.
 * Follow up:
 * Could you solve it in O(nk) runtime?
 */
public class PaintHouseII {
    public int minCostII(int[][] costs) {
        if (costs == null || costs.length < 1 || costs[0].length < 1) return 0;
        int min1 = 0, min2 = 0, minIndex = -1;
        for (int i = 0; i < costs.length; i++) {
            int currentMin1 = Integer.MAX_VALUE, currentMin2 = Integer.MAX_VALUE, currentMinIndex = -1;
            for (int j = 0; j < costs[0].length; j++) {
                int cost = costs[i][j] + (minIndex != j? min1 : min2);
                if (cost < currentMin1) {
                    currentMin2 = currentMin1; currentMin1 = cost; currentMinIndex = j;
                } else {
                    currentMin2 = Math.min(currentMin2, cost);
                }
            }
            min1 = currentMin1; min2 = currentMin2; minIndex = currentMinIndex;
        }
        return min1;
    }
}
