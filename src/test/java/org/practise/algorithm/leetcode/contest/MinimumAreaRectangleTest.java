package org.practise.algorithm.leetcode.contest;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class MinimumAreaRectangleTest {
    private  MinimumAreaRectangle obj = new MinimumAreaRectangle();

    @Test
    public void testMinimumAreaRectangle() {
        final int result = obj.minAreaRect(new int[][]{{1, 1}, {1, 3}, {3, 1}, {3, 3}, {2, 2}});
        Assert.assertEquals(result, 4);
    }
}