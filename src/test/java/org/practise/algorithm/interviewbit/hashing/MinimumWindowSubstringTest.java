package org.practise.algorithm.interviewbit.hashing;

import org.testng.Assert;
import org.testng.annotations.Test;

public class MinimumWindowSubstringTest {
    private MinimumWindowSubstring obj = new MinimumWindowSubstring();

    @Test
    public void testMinimumWindowSubstring() {
        Assert.assertEquals(obj.minWindow("ADOBECODEBANC", "ABC"), "BANC");
    }

}