package org.practise.algorithm.leetcode.dynamicprogramming.easy;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ClimbingStairsTest {
    private ClimbingStairs climbingStairs = new ClimbingStairs();

    @Test
    public void testClimbingStairs(){
        Assert.assertEquals(climbingStairs.climbStairs(4), 5);
    }
}