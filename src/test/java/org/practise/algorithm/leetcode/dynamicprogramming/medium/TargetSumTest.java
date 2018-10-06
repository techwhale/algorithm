package org.practise.algorithm.leetcode.dynamicprogramming.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class TargetSumTest {
    TargetSum obj = new TargetSum();

    @Test
    public void testTargetSum() {
        int[] nums = {5, 3, 2, 1};
        int targetSumWays = obj.findTargetSumWays(nums, 5);
        Assert.assertEquals(targetSumWays, 2);
    }

    @Test
    public void testCornerCases() {
        int[] nums = {1};
        int targetSumWays = obj.findTargetSumWays(nums, 1);
        Assert.assertEquals(targetSumWays, 1);
    }

}