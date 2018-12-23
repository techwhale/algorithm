package org.practise.algorithm.leetcode.dynamicprogramming.superhard;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TallestBillboardTest {
    private TallestBillboard obj = new TallestBillboard();

    @Test
    public void testTallestBillboard() {
        final int[] rods = {1, 2, 3, 4, 5, 6};
        final int tallestBillboard = obj.tallestBillboard(rods);
        Assert.assertEquals(tallestBillboard, 10);
    }
}