package org.practise.algorithm.leetcode.dynamicprogramming.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class LargestPlusSignTest {
    private LargestPlusSign obj = new LargestPlusSign();

    @Test
    public void testLargestPlusSign(){
        int N = 5;
        int mines[][] = {{4, 2}};
        int largestPlusSign = obj.orderOfLargestPlusSign(N, mines);
        Assert.assertEquals(largestPlusSign, 2);
    }
}