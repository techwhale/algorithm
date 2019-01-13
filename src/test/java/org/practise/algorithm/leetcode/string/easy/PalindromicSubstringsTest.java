package org.practise.algorithm.leetcode.string.easy;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PalindromicSubstringsTest {
    private PalindromicSubstrings obj = new PalindromicSubstrings();

    @Test
    public void testPalindromicSubstrings() {
        Assert.assertEquals(obj.countSubstrings("abc"), 3);
        Assert.assertEquals(obj.countSubstrings("aaa"), 6);
    }
}