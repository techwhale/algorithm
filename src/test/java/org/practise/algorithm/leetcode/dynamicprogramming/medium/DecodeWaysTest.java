package org.practise.algorithm.leetcode.dynamicprogramming.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class DecodeWaysTest {
    private DecodeWays obj = new DecodeWays();

    @Test
    public void testDecodeWays() {
        String s = "2222";
        int result = obj.numDecodings(s);
        Assert.assertEquals(result, 5);
    }

    @Test
    public void testDecodeWays2() {
        String s = "27";
        int result = obj.numDecodings(s);
        Assert.assertEquals(result, 1);
    }
}