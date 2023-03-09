package org.practise.algorithm.leetcode.array.stock;

import java.util.Arrays;

/**
 * 188. Best Time to Buy and Sell Stock IV
 *
 * You are given an integer array prices where prices[i] is the price of a given stock on the ith day, and an integer k.
 *
 * Find the maximum profit you can achieve. You may complete at most k transactions.
 *
 * Note: You may not engage in multiple transactions simultaneously (i.e., you must sell the stock before you buy again).
 *
 *
 *
 * Example 1:
 *
 * Input: k = 2, prices = [2,4,1]
 * Output: 2
 * Explanation: Buy on day 1 (price = 2) and sell on day 2 (price = 4), profit = 4-2 = 2.
 * Example 2:
 *
 * Input: k = 2, prices = [3,2,6,5,0,3]
 * Output: 7
 * Explanation: Buy on day 2 (price = 2) and sell on day 3 (price = 6), profit = 6-2 = 4. Then buy on day 5 (price = 0) and sell on day 6 (price = 3), profit = 3-0 = 3.
 *
 *
 * Constraints:
 *
 * 1 <= k <= 100
 * 1 <= prices.length <= 1000
 * 0 <= prices[i] <= 1000
 */
public class BuySellStockKTransactions {
    public int maxProfit(int k, int[] prices) {
        if (k == 0 || prices.length == 0) {
            return 0;
        }
        if (k >= prices.length/2) {
            int maxProfit = 0;
            for (int i = 1; i < prices.length; i++) {
                maxProfit += Math.max(0, prices[i] - prices[i - 1]);
            }
            return maxProfit;
        }

        int[] buy = new int[k];
        int[] sell = new int[k];
        Arrays.fill(buy, Integer.MIN_VALUE);
        for (int price : prices) {
            for (int i = 0; i < k; i++) {
                buy[i] = Math.max(buy[i], (i == 0)? (- price) : (sell[i - 1] - price));
                sell[i] = Math.max(sell[i], buy[i] + price);
            }
        }
        return sell[k - 1];
    }
}
