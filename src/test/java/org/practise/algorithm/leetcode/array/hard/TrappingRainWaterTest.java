package org.practise.algorithm.leetcode.array.hard;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class TrappingRainWaterTest {
    @Test
    public void testRainTrap(){
        TrappingRainWater obj = new TrappingRainWater();
        int[] values = {4,2,0,3,2,5};
        Assert.assertEquals(obj.trap(values), 9);
        int[] values2 = {0,1,0,2,1,0,1,3,2,1,2,1};
        Assert.assertEquals(obj.trap(values2), 6);
    }
}