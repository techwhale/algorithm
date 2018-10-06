package org.practise.algorithm.leetcode.dynamicprogramming.hard;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class DecodeWaysIITest {
    private DecodeWaysII obj = new DecodeWaysII();

    @Test
    public void testDecodeWays() {
        String str = "*3";
        int decodings = obj.numDecodings(str);
        Assert.assertEquals(decodings, 11);
    }


    @Test
    public void testDecodeWays2() {
        String str = "2*1*71";
        int decodings = obj.numDecodings(str);
        Assert.assertEquals(decodings, 305);
    }
}