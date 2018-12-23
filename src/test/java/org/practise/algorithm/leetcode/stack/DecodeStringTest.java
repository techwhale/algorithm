package org.practise.algorithm.leetcode.stack;

import org.practise.algorithm.leetcode.hardcorecoding.DecodeString;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DecodeStringTest {
    private DecodeString obj = new DecodeString();

    @Test
    public void testDecodeString() {
        Assert.assertEquals(obj.decodeString("3[a2[c]]"), "accaccacc");
    }
}