package org.practise.algorithm.leetcode.string.hard;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class MinimumWindowSubstringTest {

    private MinimumWindowSubstring minimumWindowSubstring = new MinimumWindowSubstring();

    @Test
    public void testMinWindow() {
        Assert.assertEquals(minimumWindowSubstring.minWindow("ADOBECODEBANC", "ABC"), "BANC");
    }
}