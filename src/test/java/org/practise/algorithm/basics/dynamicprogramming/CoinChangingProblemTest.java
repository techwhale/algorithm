package org.practise.algorithm.basics.dynamicprogramming;

import org.testng.Assert;
import org.testng.annotations.Test;

public class CoinChangingProblemTest {
    private CoinChangingProblem obj = new CoinChangingProblem();
    @Test
    public void testCoinChangingProblem() {
        Assert.assertEquals(obj.findMinimumCoins(new int[]{7, 2, 3, 6}, 13), 2);
    }
}