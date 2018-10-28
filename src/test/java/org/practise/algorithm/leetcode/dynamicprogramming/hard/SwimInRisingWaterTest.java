package org.practise.algorithm.leetcode.dynamicprogramming.hard;

import org.practise.algorithm.leetcode.priorityqueue.SwimInRisingWater;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SwimInRisingWaterTest {
    private SwimInRisingWater obj = new SwimInRisingWater();

    @Test
    public void testSwimInRisingWater() {
        final int[][] grid = {{0, 1, 2, 3, 4}, {24, 23, 22, 21, 5}, {12, 13, 14, 15, 16}, {11, 17, 18, 19, 20}, {10, 9, 8, 7, 6}};
        final int result = obj.swimInWater(grid);
        Assert.assertEquals(result, 16);
    }

    @Test
    public void testSwimInRisingWater2() {
        final int[][] grid = {{0, 2}, {1, 3}};
        final int result = obj.swimInWater(grid);
        Assert.assertEquals(result, 3);
    }
}