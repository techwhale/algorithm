package org.practise.algorithm.leetcode.string.hard;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ScrambleStringTest {
    private ScrambleString obj = new ScrambleString();

    @Test
    public void testScrambleString() {
        // Assert.assertTrue(obj.isScramble("great", "rgeat"));
        Assert.assertTrue(obj.isScramble("abc", "bac"));

    }
}