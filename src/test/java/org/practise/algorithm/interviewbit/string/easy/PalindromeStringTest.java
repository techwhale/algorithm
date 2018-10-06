package org.practise.algorithm.interviewbit.string.easy;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class PalindromeStringTest {
    PalindromeString palindrome = new PalindromeString();

    @Test
    public void testPalindrome() {
        String string = "A man, a plan, a canal: Panama";
        int palindrome = this.palindrome.isPalindrome(string);
        Assert.assertEquals(palindrome, 1);
    }

    @Test
    public void testCases() {
        String string = "1a2";
        int palindrome = this.palindrome.isPalindrome(string);
        Assert.assertEquals(palindrome, 0);
    }
}