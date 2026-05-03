package org.practise.algorithm.leetcode.binarysearch.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class FindFirstAndLastElementTest {

    private FindFirstAndLastElement obj = new FindFirstAndLastElement();

    @Test
    public void testSearchRange() {
        Assert.assertEquals(obj.searchRange(new int[] {5,7,7,8,8,10}, 15), new int[] { -1, -1});
    }

    @Test
    public void testSearchRange2() {
        Assert.assertEquals(obj.searchRange(new int[] {5,7,7,8,8,10}, 8), new int[] { 3, 4});
    }
}