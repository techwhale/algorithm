package org.practise.algorithm.interviewbit.dynamicprogramming;

import org.testng.Assert;
import org.testng.annotations.Test;

public class RegularExpressionITest {
    private RegularExpressionI obj = new RegularExpressionI();

    @Test
    public void testRegularExpressionMatching() {

        /**
         *  * isMatch("aa","a") → 0
         *  * isMatch("aa","aa") → 1
         *  * isMatch("aaa","aa") → 0
         *  * isMatch("aa", "*") → 1
         *  * isMatch("aa", "a*") → 1
         *  * isMatch("ab", "?*") → 1
         *  * isMatch("aab", "c*a*b") → 0
         */

        Assert.assertFalse(obj.isMatch("aa", "a"));
        Assert.assertFalse(obj.isMatch("aaa", "aaaa"));
        Assert.assertTrue(obj.isMatch("aa", "a*"));
        Assert.assertTrue(obj.isMatch("aa", "aa"));
        Assert.assertTrue(obj.isMatch("aa", "*"));
        Assert.assertTrue(obj.isMatch("ab", "?*"));
        Assert.assertFalse(obj.isMatch("aab", "c*a*b"));
    }
}