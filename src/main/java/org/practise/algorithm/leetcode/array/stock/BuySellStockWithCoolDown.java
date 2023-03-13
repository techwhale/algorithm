package org.practise.algorithm.leetcode.array.stock;

/*
Say you have an array for which the ith element is the price of a given stock on day i.

Design an algorithm to find the maximum profit. You may complete as many transactions as you like
(ie, buy one and sell one share of the stock multiple times) with the following restrictions:

You may not engage in multiple transactions at the same time (ie, you must sell the stock before you buy again).
After you sell your stock, you cannot buy stock on next day. (ie, cooldown 1 day)
Example:

Input: [1,2,3,0,2]
Output: 3
Explanation: transactions = [buy, sell, cooldown, buy, sell]

 */
public class BuySellStockWithCoolDown {
    public int maxProfit(int[] prices) {
        int buy = Integer.MIN_VALUE;
        int sell = 0;
        int cooldown = 0;
        for (int i = 0; i < prices.length; i++) {
            int prevBuy = buy;
            int prevSell = sell;
            int prevCoolDown = cooldown;
            buy = Math.max(buy, prevCoolDown - prices[i]);
            sell = Math.max(prevSell, prevBuy + prices[i]);
            cooldown = Math.max(prevCoolDown, prevSell);
        }
        return Math.max(cooldown, sell);
    }
}
