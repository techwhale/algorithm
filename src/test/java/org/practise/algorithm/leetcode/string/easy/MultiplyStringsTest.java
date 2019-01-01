package org.practise.algorithm.leetcode.string.easy;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class MultiplyStringsTest {
    private MultiplyStrings obj = new MultiplyStrings();
    @Test
    public void testMultiply() {
        final String multiply = obj.multiply("2", "3");
        Assert.assertEquals(multiply, "6");
    }
}