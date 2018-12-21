package org.practise.algorithm.leetcode.interestingsolution;

import org.practise.algorithm.interestingideas.CandyDistribution;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CandyDistributionTest {
    private CandyDistribution obj = new CandyDistribution();

    @Test
    public void testCandyDistribution() {
        final int[] candies = {1, 0, 2};
        final int totalCandies = obj.candy(candies);
        Assert.assertEquals(totalCandies, 5);
    }

    @Test
    public void testCandyDistribution2() {
        final int[] candies = {1, 2, 2};
        final int totalCandies = obj.candy(candies);
        Assert.assertEquals(totalCandies, 4);
    }

    @Test
    public void testCandyDistribution3() {
        final int[] candies = {12, 4, 3, 11, 34, 34, 1, 67};
        final int totalCandies = obj.candy(candies);
        Assert.assertEquals(totalCandies, 16);
    }
}
