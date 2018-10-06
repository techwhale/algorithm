package org.practise.algorithm.leetcode.priorityqueue;

import org.testng.Assert;
import org.testng.annotations.Test;

public class RainWaterTrappingTest {

    RainWaterTrapping rainWaterTrapping = new RainWaterTrapping();
    @Test
    public void testTrappingRainWater() {
        int[][] cellheight = new int[][] {
                {1,4,3,1,3,2},
                {3,2,1,3,2,4},
                {2,3,3,2,3,1}
        };
        int maxTrapped = rainWaterTrapping.trapRainWater(cellheight);
        Assert.assertEquals(maxTrapped, 4);
    }
}