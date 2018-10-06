package org.practise.algorithm.leetcode.dynamicprogramming.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

public class CoinChangeTest {
    CoinChange obj = new CoinChange();

    @Test
    public void testCoinChange() {
        int[] coins = new int[] {1, 2, 5};
        int numberOfCoin = obj.coinChange(coins, 11);
        Assert.assertEquals(numberOfCoin, 3);
    }

    @Test
    public void testCoinChange2() {
        int[] coins = new int[] {2};
        int numberOfCoin = obj.coinChange(coins, 3);
        Assert.assertEquals(numberOfCoin,  -1);
    }
}