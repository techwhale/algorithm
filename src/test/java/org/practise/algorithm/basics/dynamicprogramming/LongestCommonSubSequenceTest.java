package org.practise.algorithm.basics.dynamicprogramming;

import org.testng.Assert;
import org.testng.annotations.Test;

public class LongestCommonSubSequenceTest {
    private LongestCommonSubSequence obj = new LongestCommonSubSequence();

    @Test
    public void testLongestCommonSubSequence() {
        final int count = obj.findLongestCommonSubSequence("abcdaf", "acbcf");
        Assert.assertEquals(count, 4);
    }
}