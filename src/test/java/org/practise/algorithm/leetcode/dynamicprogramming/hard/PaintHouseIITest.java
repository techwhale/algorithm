package org.practise.algorithm.leetcode.dynamicprogramming.hard;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class PaintHouseIITest {
    private PaintHouseII obj = new PaintHouseII();

    @Test
    public void testPaintHouse() {
        final int[][] costs = {{1, 5, 3}, {2, 9, 4}};
        Assert.assertEquals(obj.minCostII(costs), 5);
    }

    @Test
    public void testCornerCase() {
        final int[][] costs = {{8}};
        Assert.assertEquals(obj.minCostII(costs), 8);
    }
}