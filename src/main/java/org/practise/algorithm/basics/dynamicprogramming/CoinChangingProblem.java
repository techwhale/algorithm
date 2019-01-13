package org.practise.algorithm.basics.dynamicprogramming;

import java.util.Arrays;

public class CoinChangingProblem {
    public int findMinimumCoins(int[] coins, int target) {
        int[] dp = new int[target + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;

        for (int i = 0; i < coins.length; i++) {
            int coin = coins[i];
            for (int t = coin; t <= target; t++) {
                if (dp[t - coin] != Integer.MAX_VALUE) {
                    dp[t] = Math.min(1 + dp[t - coin], dp[t]);
                }
            }
        }
        return dp[target] == Integer.MAX_VALUE ? 0 : dp[target];
    }
}
