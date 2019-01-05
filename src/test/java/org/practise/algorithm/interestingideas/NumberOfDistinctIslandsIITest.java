package org.practise.algorithm.interestingideas;

import org.testng.Assert;
import org.testng.annotations.Test;

public class NumberOfDistinctIslandsIITest {
    private NumberOfDistinctIslandsII obj = new NumberOfDistinctIslandsII();

    @Test
    public void testNumberOfDistinctIslandsII() {
        final int[][] grid = {{1, 1, 0, 0, 0}, {1, 0, 0, 0, 0}, {0, 0, 0, 0, 1}, {0, 0, 0, 1, 1}};
        Assert.assertEquals(obj.numDistinctIslands2(grid), 1);
    }
}