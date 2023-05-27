package org.practise.algorithm.leetcode.segmenttree;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class LongestIncreasingSubsequenceIITest {
    private LongestIncreasingSubsequenceII obj = new LongestIncreasingSubsequenceII();
    @Test
    public void testLengthOfLIS() {
        Assert.assertEquals(obj.lengthOfLIS(new int[]{4,2,1,4,3,4,5,8,15}, 3), 5);
    }
}