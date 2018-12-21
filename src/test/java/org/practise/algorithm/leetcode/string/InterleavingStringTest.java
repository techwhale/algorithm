package org.practise.algorithm.leetcode.string;

import org.practise.algorithm.leetcode.string.hard.InterleavingString;
import org.testng.Assert;
import org.testng.annotations.Test;

public class InterleavingStringTest {
    InterleavingString obj = new InterleavingString();

    @Test
    public void testInterleavingString() {
        String s1 = "aabcc", s2 = "dbbca", s3 = "aadbbcbcac";
        boolean interleave = obj.isInterleave(s1, s2, s3);
        Assert.assertEquals(interleave, true);
    }

    @Test
    public void testCornerCases() {
        String s1 = "a", s2 = "", s3 = "a";
        boolean interleave = obj.isInterleave(s1, s2, s3);
        Assert.assertEquals(interleave, true);
    }

    @Test
    public void testCornerCases2() {
        String s1 = "c", s2 = "", s3 = "a";
        boolean interleave = obj.isInterleave(s1, s2, s3);
        Assert.assertEquals(interleave, false);
    }
}