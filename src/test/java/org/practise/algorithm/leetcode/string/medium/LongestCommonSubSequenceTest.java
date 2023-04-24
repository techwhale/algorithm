package org.practise.algorithm.leetcode.string.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class LongestCommonSubSequenceTest {
    private LongestCommonSubSequence obj = new LongestCommonSubSequence();
    @Test
    public void testLongestCommonSubsequence() {
        Assert.assertEquals(obj.longestCommonSubsequence("actgattag", "gtgtgatcg"), 5);
    }
}