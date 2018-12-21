package org.practise.algorithm.interviewbit.dynamicprogramming;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class LongestArithmeticProgressionTest {
    private LongestArithmeticProgression obj = new LongestArithmeticProgression();

    @Test
    public void testLongestArithmeticProgression() {
        final int[] nums = {9, 4, 7, 2, 10};
        final int result = obj.solve(nums);
        Assert.assertEquals(result, 3);
    }
}