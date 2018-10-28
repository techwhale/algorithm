package org.practise.algorithm.leetcode.string.hard;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class RearrangeStringKDistanceApartTest {
    private RearrangeStringKDistanceApart obj = new RearrangeStringKDistanceApart();

    @Test
    public void testRearrangeStringKDistanceApart() {
        Assert.assertEquals(obj.rearrangeString("aabbcc", 3), "acbacb");
    }

    @Test
    public void testRearrangeStringKDistanceApart2() {
        Assert.assertEquals(obj.rearrangeString("aaabc", 3), "");
    }

    @Test
    public void testRearrangeStringKDistanceApart3() {
        Assert.assertEquals(obj.rearrangeString("aaadbbcc", 2), "abcabcad");
    }

    @Test
    public void testRearrangeStringKDistanceApart4() {
        Assert.assertEquals(obj.rearrangeString("aa", 2), "");
    }

    @Test
    public void testRearrangeStringKDistanceApart5() {
        Assert.assertEquals(obj.rearrangeString("aa", 0), "aa");
    }
}