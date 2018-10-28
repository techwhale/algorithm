package org.practise.algorithm.leetcode.dynamicprogramming.hard;

import org.testng.Assert;
import org.testng.annotations.Test;

public class BurstBalloonsTest {
    private BurstBalloons obj = new BurstBalloons();
    @Test
    public void testBurstBalloons() {
        int num[] = {3,1,5,8};
        Assert.assertEquals( obj.maxCoins(num), 167);
    }

}