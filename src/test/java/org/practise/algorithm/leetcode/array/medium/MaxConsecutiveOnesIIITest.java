package org.practise.algorithm.leetcode.array.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class MaxConsecutiveOnesIIITest {
    private MaxConsecutiveOnesIII obj = new MaxConsecutiveOnesIII();

    @Test
    public void testMaxConsecutiveOnes() {
        int[] nums = new int[] {1,1,1,0,0,0,1,1,1,1,0};
        Assert.assertEquals(obj.longestOnes(nums, 2), 6);
    }
}