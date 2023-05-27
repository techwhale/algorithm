package org.practise.algorithm.leetcode.string.hard;


import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class RegexPatternMatcherTest {
    RegexPatternMatcher obj = new RegexPatternMatcher();
    @Test
    public void testIsMatch() {
        Assert.assertTrue(obj.isMatch("abbbbbbbbbbbc", "ab*c"));
    }
}