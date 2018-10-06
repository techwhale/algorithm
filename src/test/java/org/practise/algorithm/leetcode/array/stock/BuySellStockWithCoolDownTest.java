package org.practise.algorithm.leetcode.array.stock;

import org.testng.Assert;
import org.testng.annotations.Test;

public class BuySellStockWithCoolDownTest {
    BuySellStockWithCoolDown stockWithCoolDown = new BuySellStockWithCoolDown();

    @Test
    public void testCoolDown() {
        int[] prices = new int[] {1,2,3,0,2};
        int maxProfit = stockWithCoolDown.maxProfit(prices);
        Assert.assertEquals(maxProfit, 3);
    }
}