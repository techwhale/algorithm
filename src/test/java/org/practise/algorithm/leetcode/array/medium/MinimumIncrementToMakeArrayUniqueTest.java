package org.practise.algorithm.leetcode.array.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

public class MinimumIncrementToMakeArrayUniqueTest {
    private MinimumIncrementToMakeArrayUnique obj = new MinimumIncrementToMakeArrayUnique();

    @Test
    public void testMinimumIncrementToMakeArrayUnique() {
        final int result = obj.minIncrementForUnique(new int[]{1, 2, 2});
        Assert.assertEquals(result, 1);
    }

    @Test
    public void testMinimumIncrementToMakeArrayUnique2() {
        final int result = obj.minIncrementForUnique(new int[]{3,2,1,2,1,7});
        Assert.assertEquals(result, 6);
    }
}