package org.practise.algorithm.leetcode.dynamicprogramming.hard;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class MaximumRectangleTest {
    private MaximumRectangle obj = new MaximumRectangle();

    @Test
    public void testMaximumRectangle() {
        char[][] arr = new char[][] {
                {'1','0','1','0','0'},
                {'1','0','1','1','1'},
                {'1','1','1','1','1'},
                {'1','0','0','1','0'}
        };
        int maximalRectangle = obj.maximalRectangle(arr);
        Assert.assertEquals(maximalRectangle, 6);
    }
}