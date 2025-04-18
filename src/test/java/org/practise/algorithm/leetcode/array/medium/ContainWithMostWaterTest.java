package org.practise.algorithm.leetcode.array.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ContainWithMostWaterTest {
    private  ContainWithMostWater obj = new ContainWithMostWater();
    @Test
    public void testMaxArea() {
        int[] height = {1,8,6,2,5,4,8,3,7};
        Assert.assertEquals(obj.maxArea(height), 49);
    }

    @Test
    public void testMaxArea2() {
        int[] height = {1,1};
        Assert.assertEquals(obj.maxArea(height), 1);
    }
}