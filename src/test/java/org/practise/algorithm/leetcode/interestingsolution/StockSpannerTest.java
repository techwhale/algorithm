package org.practise.algorithm.leetcode.interestingsolution;

import org.testng.Assert;
import org.testng.annotations.Test;

public class StockSpannerTest {
    private StockSpanner obj = new StockSpanner();

    @Test
    public void testStockSpanner() {
        Assert.assertEquals(obj.next(100), 1);
        Assert.assertEquals(obj.next(80), 1);
        Assert.assertEquals(obj.next(60), 1);
        Assert.assertEquals(obj.next(70), 2);
        Assert.assertEquals(obj.next(60), 1);
        Assert.assertEquals(obj.next(75), 4);
        Assert.assertEquals(obj.next(85), 6);
    }
}