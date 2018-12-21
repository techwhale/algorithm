package org.practise.algorithm.leetcode.dynamicprogramming.hard;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PalindromePartitioningIITest {
    private PalindromePartitioningII obj = new PalindromePartitioningII();

    @Test
    public void testPalindromePartitioning1() {
        Assert.assertEquals(obj.minCut("abbac"), 1);
    }

    @Test
    public void testPalindromePartitioning2() {
        Assert.assertEquals(obj.minCut("a"), 0);
    }

    @Test
    public void testPalindromePartitioning3() {
        Assert.assertEquals(obj.minCut("aba"), 0);
    }

}