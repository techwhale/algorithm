package org.practise.algorithm.basics.dynamicprogramming;

public class Knapsack {
    public int knapsack(int[] weight, int[] value, int W) {
        if (weight == null || weight.length == 0 || value == null || value.length == 0 || W == 0)
            return 0;
        int N = weight.length;
        int[][] dp = new int[N + 1][W + 1];
        for (int i = 0; i <= N; i++) {
            for (int w = 0; w <= W; w++) {
                if (i == 0 || w == 0) {
                    dp[i][w] = 0;
                    continue;
                }

                if (weight[i - 1] <= w) {
                    dp[i][w] = Math.max(value[i - 1] + dp[i - 1][w - weight[i - 1]], dp[i -1][w]);
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }
        return dp[N][W];
    }
}
