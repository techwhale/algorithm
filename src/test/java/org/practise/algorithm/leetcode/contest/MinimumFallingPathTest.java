package org.practise.algorithm.leetcode.contest;

import org.practise.algorithm.leetcode.dynamicprogramming.easy.MinimumFallingPath;
import org.testng.Assert;
import org.testng.annotations.Test;

public class MinimumFallingPathTest {
    private MinimumFallingPath obj = new MinimumFallingPath();

    @Test
    public void testMinimumFallingPath() {
        final int[][] ints = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        final int pathSum = obj.minFallingPathSum(ints);
        Assert.assertEquals(pathSum, 12);
    }
}