package org.practise.algorithm.leetcode.dynamicprogramming.practise;

import org.practise.algorithm.leetcode.interestingsolution.NumberOfCornerRectangles;
import org.testng.Assert;
import org.testng.annotations.Test;

public class NumberOfCornerRectanglesTest {
    private NumberOfCornerRectangles obj = new NumberOfCornerRectangles();

    @Test
    public void testNumberOfCornerRectangles() {
        final int[][] grid = {{1, 0, 0, 1, 0},
                {0, 0, 1, 0, 1},
                {0, 0, 0, 1, 0},
                {1, 0, 1, 0, 1}};
        final int cornerRectangles = obj.countCornerRectangles(grid);
        Assert.assertEquals(cornerRectangles, 1);
    }
}