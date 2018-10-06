package org.practise.algorithm.leetcode.dynamicprogramming.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class MaximalSquareTest {
    private MaximalSquare obj = new MaximalSquare();

    @Test
    public void testMaximalSquare() {
        char[][] chars = new char[][] {
                {'1', '0', '1', '0', '0'},
                {'1', '0', '1', '1', '1'},
                {'1', '1', '1', '1', '1'},
                {'1', '0', '0', '1', '0'}
        };
        int result = obj.maximalSquare(chars);
        Assert.assertEquals(result, 4);
    }
}