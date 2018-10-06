package org.practise.algorithm.leetcode.array.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class SubarraySumEqualsKTest {
    SubarraySumEqualsK obj = new SubarraySumEqualsK();

    @Test
    public void testSubarraySumEqualsK() {
        int[] arr = new int[]{1, 1, 1};
        int result = obj.subarraySum(arr, 2);
        Assert.assertEquals(2, result);
    }

    @Test
    public void testCornerCases() {
        int[] arr = new int[]{0, 0, 0};
        int result = obj.subarraySum(arr, 0);
        Assert.assertEquals(result, 6);
    }
}