package org.practise.algorithm.leetcode.string;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ReorganizeStringTest {
    private ReorganizeString reorganizeString = new ReorganizeString();

    @Test
    public void testReorganizeString() {
        Assert.assertEquals(reorganizeString.reorganizeString("aab"), "aba");
    }

    @Test
    public void testReorganizeString2() {
        Assert.assertEquals(reorganizeString.reorganizeString("aaab"), "");
    }

    @Test
    public void testReorganizeString3() {
        Assert.assertEquals(reorganizeString.reorganizeString("aaaabc"), "");
    }

    @Test
    public void testReorganizeString4() {
        Assert.assertEquals(reorganizeString.reorganizeString("aaabc"), "acaba");
    }
}