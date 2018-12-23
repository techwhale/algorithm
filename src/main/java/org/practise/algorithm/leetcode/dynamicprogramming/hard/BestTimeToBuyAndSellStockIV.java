package org.practise.algorithm.leetcode.dynamicprogramming.hard;

/**
 * Say you have an array for which the ith element is the price of a given stock on day i.
 * <p>
 * Design an algorithm to find the maximum profit. You may complete at most k transactions.
 * <p>
 * Note:
 * You may not engage in multiple transactions at the same time (ie, you must sell the stock before you buy again).
 * <p>
 * Example 1:
 * <p>
 * Input: [2,4,1], k = 2
 * Output: 2
 * Explanation: Buy on day 1 (price = 2) and sell on day 2 (price = 4), profit = 4-2 = 2.
 * Example 2:
 * <p>
 * Input: [3,2,6,5,0,3], k = 2
 * Output: 7
 * Explanation: Buy on day 2 (price = 2) and sell on day 3 (price = 6), profit = 6-2 = 4.
 * Then buy on day 5 (price = 0) and sell on day 6 (price = 3), profit = 3-0 = 3.
 */
public class BestTimeToBuyAndSellStockIV {

    public int maxProfit(int K, int[] prices) {
        if (prices == null || prices.length <= 1) return 0;

        if (K >= prices.length / 2) {
            return findMaxProfit(prices);
        }
        int[] dp = new int[prices.length];
        for (int k = 1; k <= K; k++) {
            int[] dp2 = new int[prices.length];
            int localMax = dp[0] - prices[0];
            for (int i = 1; i < prices.length; i++) {
                dp2[i] = Math.max(dp2[i - 1], prices[i] + localMax);
                localMax = Math.max(localMax, dp[i] - prices[i]);
            }
            dp = dp2;
        }
        return dp[dp.length - 1];
    }

    private int findMaxProfit(int[] prices) {
        int maxProfit = 0;
        for (int i = 1; i < prices.length; i++) {
            maxProfit += Math.max(0, prices[i] - prices[i - 1]);
        }
        return maxProfit;
    }

}
