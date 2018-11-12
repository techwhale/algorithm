package org.practise.algorithm.leetcode.dynamicprogramming.hard;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PalindromePartitioningIITest {
    private PalindromePartitioningII obj = new PalindromePartitioningII();

    @Test
    public void testPalindromePartitioning1() {
        Assert.assertEquals(obj.minCut("abcbm"), 2);
    }

}