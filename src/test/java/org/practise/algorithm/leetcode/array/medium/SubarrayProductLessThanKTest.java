package org.practise.algorithm.leetcode.array.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SubarrayProductLessThanKTest {
    SubarrayProductLessThanK obj = new SubarrayProductLessThanK();

    @Test
    public void testSubArrayProductLessThanK() {
        int[] arr = new int[]{10, 5, 2, 6};
        int output = obj.numSubarrayProductLessThanK(arr, 100);
        Assert.assertEquals(output, 8);
    }
}