package org.practise.algorithm.leetcode.string.hard;

import org.testng.Assert;
import org.testng.annotations.Test;

public class WordPatternIITest {
    private WordPatternII obj = new WordPatternII();

    @Test
    public void testWordPatternII() {
        String pattern = "abab", str = "redblueredblue";
        Assert.assertTrue(obj.wordPatternMatch(pattern, str));
    }

    @Test
    public void test2WordPatternII() {
        String pattern = "aaaa", str = "asdasdasdasd";
        Assert.assertTrue(obj.wordPatternMatch(pattern, str));
    }

    @Test
    public void test3WordPatternII() {
        String pattern = "aabb", str = "xyzabcxzyabc";
        Assert.assertFalse(obj.wordPatternMatch(pattern, str));
    }
}