package org.practise.algorithm.leetcode.dynamicprogramming.practise;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class LongestPalindromicSubsequenceTest {

    private  LongestPalindromicSubsequence obj = new LongestPalindromicSubsequence();
    @Test
    public void testLongestPalindromeSubseq() {
        Assert.assertEquals(obj.longestPalindromeSubseq("bbbab"), 4);
    }
}