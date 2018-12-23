package org.practise.algorithm.leetcode.string.easy;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ValidPalindromeTest {
    private ValidPalindrome obj = new ValidPalindrome();

    @Test
    public void testValidPalindrome() {
        Assert.assertTrue(obj.validPalindrome("abca"));
    }

    @Test
    public void testIsPalindrome() {
        Assert.assertFalse(obj.isPalindrome("OP"));
    }

}