package org.practise.algorithm.leetcode.binarysearch.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SearchIn2DTest {
    private SearchIn2D obj = new SearchIn2D();
    @Test
    public void testSearchIn2D() {
        int[][] arr =
                {{1,   3,  5,  7},
                {10, 11, 16, 20},
                {23, 30, 34, 50}};

        Assert.assertTrue(obj.searchMatrix(arr, 3));
        Assert.assertFalse(obj.searchMatrix(arr, 2));
    }
}