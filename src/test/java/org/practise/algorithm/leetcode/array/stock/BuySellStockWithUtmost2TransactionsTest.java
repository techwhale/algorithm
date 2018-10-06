package org.practise.algorithm.leetcode.array.stock;

import org.testng.Assert;
import org.testng.annotations.Test;

public class BuySellStockWithUtmost2TransactionsTest {
    BuySellStockWithUtmost2Transactions obj = new BuySellStockWithUtmost2Transactions();

    @Test
    public void testUtmost2MaxTransactions() {
        int[] stocks = new int[] {3,3,5,0,0,3,1,4,1,10,1,10};
        int maxProfit = obj.maxProfit(stocks);
        Assert.assertEquals(maxProfit, 19);
    }
}