package org.practise.algorithm.leetcode.array.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ContinuousSubarraySumTest {
    ContinuousSubarraySum obj = new ContinuousSubarraySum();

    @Test
    public void testContinuousSubarraySum() {
        int [] arr = new int[] {23, 2, 6, 4, 7};
        boolean result = obj.checkSubarraySum(arr, 6);
        Assert.assertEquals(result, true);
    }

    @Test
    public void testCornerCases() {
        int arr[] = new int[] {0, 0};
        boolean result = obj.checkSubarraySum(arr, 0);
        Assert.assertEquals(result, true);
    }
}