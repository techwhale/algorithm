package org.practise.algorithm.interviewbit.dynamicprogramming;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class RegularExpressionIITest {
    private RegularExpressionII obj = new RegularExpressionII();
    @Test
    public void testRegularExpressionII() {
        /**
         *  * isMatch("aa","a") → 0
         *  * isMatch("aa","aa") → 1
         *  * isMatch("aaa","aa") → 0
         *  * isMatch("aa", "a*") → 1
         *  * isMatch("aa", ".*") → 1
         *  * isMatch("ab", ".*") → 1
         *  * isMatch("aab", "c*a*b") → 1
         */

        Assert.assertFalse(obj.isMatch("aa", "a"));
        Assert.assertFalse(obj.isMatch("aaa", "aaaa"));
        Assert.assertTrue(obj.isMatch("aa", "a*"));
        Assert.assertTrue(obj.isMatch("aa", "aa"));
        Assert.assertTrue(obj.isMatch("ab", ".*"));
        Assert.assertTrue(obj.isMatch("aab", "c*a*b"));
        Assert.assertTrue(obj.isMatch("aaa", "ab*ac*a"));
        Assert.assertTrue(obj.isMatch("mississippi", "mis*is*ip*."));
        Assert.assertFalse(obj.isMatch("mississippi", "mis*is*p*."));
    }
}