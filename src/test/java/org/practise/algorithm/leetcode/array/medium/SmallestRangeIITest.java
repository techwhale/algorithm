package org.practise.algorithm.leetcode.array.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SmallestRangeIITest {
    SmallestRangeII obj = new SmallestRangeII();

    @Test
    public void testSmallestRange() {
        int[] arr = new int[] {1,3,6};
        int result = obj.smallestRangeII(arr, 3);
        Assert.assertEquals(result, 3);
    }
}