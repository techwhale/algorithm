package org.practise.algorithm.leetcode.array.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

public class MinimumNumberOfArrowsToBurstBalloonsTest {

    private MinimumNumberOfArrowsToBurstBalloons obj = new MinimumNumberOfArrowsToBurstBalloons();
    @Test
    public void testMinimumNumberOfArrowsToBurstBalloons() {
        final int[][] points = {{10, 16}, {2, 8}, {1, 6}, {7, 12}};
        final int minArrowShots = obj.findMinArrowShots(points);
        Assert.assertEquals(minArrowShots, 2);
    }

    @Test
    public void testCornerCases() {
        final int[][] points = {{-2147483648,2147483647}};
        final int minArrowShots = obj.findMinArrowShots(points);
        Assert.assertEquals(minArrowShots, 1);
    }
}