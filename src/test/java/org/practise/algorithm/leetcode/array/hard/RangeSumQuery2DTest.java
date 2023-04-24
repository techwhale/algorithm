package org.practise.algorithm.leetcode.array.hard;

import org.practise.algorithm.leetcode.interestingsolution.RangeSumQuery2D;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RangeSumQuery2DTest {

    @Test
    public void testCode() {
        int[][] matrix = {{3, 0, 1, 4, 2}, {5, 6, 3, 2, 1}, {1, 2, 0, 1, 5}, {4, 1, 0, 1, 7}, {1, 0, 3, 0, 5}};
        RangeSumQuery2D obj = new RangeSumQuery2D(matrix);
        Assert.assertEquals(obj.sumRegion(2, 1, 4, 3), 8);
        obj.update(3, 2, 2);
        Assert.assertEquals(obj.sumRegion(2, 1, 4, 3), 10);
    }
}