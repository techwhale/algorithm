package org.practise.algorithm.leetcode.dynamicprogramming.superhard;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ProfitableSchemesTest {
    private ProfitableSchemes obj = new ProfitableSchemes();

    @Test
    public void testProfitableSchemes() {
        final int profit = obj.profitableSchemes(5, 3, new int[]{2, 2}, new int[]{2, 3});
        Assert.assertEquals(profit, 2);
    }
}