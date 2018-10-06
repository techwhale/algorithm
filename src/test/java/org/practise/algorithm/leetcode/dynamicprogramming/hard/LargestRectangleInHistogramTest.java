package org.practise.algorithm.leetcode.dynamicprogramming.hard;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class LargestRectangleInHistogramTest {
    private LargestRectangleInHistogram obj = new LargestRectangleInHistogram();
    @Test
    public void testLargestRectangleInHistogram() {
        int[] array = {2, 1, 5, 6, 2, 3};
        int largestRectangleArea = obj.largestRectangleArea(array);
        Assert.assertEquals(largestRectangleArea, 10);
    }
}