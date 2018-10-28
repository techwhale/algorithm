package org.practise.algorithm.leetcode.contest;

import org.practise.algorithm.leetcode.string.LongPressedName;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LongPressedNameTest {
    private LongPressedName obj = new LongPressedName();

    @Test
    public void testLongPressedName() {
        String name = "kikcxmvzi", typed = "kiikcxxmmvvzz";
        Assert.assertFalse(obj.isLongPressedName(name, typed));
    }

    @Test
    public void testLongPressedName2() {
        String name  = "alex", typed = "aaleex";
        Assert.assertTrue(obj.isLongPressedName(name, typed));
    }

    @Test
    public void testLongPressedName3() {
        String name  = "saeed", typed = "ssaaedd";
        Assert.assertFalse(obj.isLongPressedName(name, typed));
    }

    @Test
    public void testLongPressedName4() {
        String name  = "leelee", typed = "lleeelee";
        Assert.assertTrue(obj.isLongPressedName(name, typed));
    }

    @Test
    public void testLongPressedName5() {
        String name  = "laiden", typed = "laiden";
        Assert.assertTrue(obj.isLongPressedName(name, typed));
    }
}