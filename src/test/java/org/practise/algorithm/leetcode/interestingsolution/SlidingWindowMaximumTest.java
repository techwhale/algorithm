package org.practise.algorithm.leetcode.interestingsolution;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.*;

public class SlidingWindowMaximumTest {
    private SlidingWindowMaximum obj = new SlidingWindowMaximum();

    @Test
    public void testSlidingWindowMaximum() {
        int[] nums = new int[] {1,3,-1,-3,5,3,6,7};
        final int[] actual = obj.maxSlidingWindow(nums, 3);
        Assert.assertEquals(Arrays.toString(actual), Arrays.toString(new int[] {3,3,5,5,6,7}));
    }

    @Test
    public void testCornerCases() {
        int[] nums = new int[] {5, 3, 4};
        final int[] actual = obj.maxSlidingWindow(nums, 1);
        Assert.assertEquals(Arrays.toString(actual), Arrays.toString(new int[] {5, 3, 4}));
    }
}