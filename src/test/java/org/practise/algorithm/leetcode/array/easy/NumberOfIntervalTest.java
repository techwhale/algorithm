package org.practise.algorithm.leetcode.array.easy;

import org.practise.algorithm.leetcode.binarysearch.medium.NumberOfInterval;
import org.testng.Assert;
import org.testng.annotations.Test;

public class NumberOfIntervalTest {

    private NumberOfInterval obj = new NumberOfInterval();
    @Test
    public void test() {
        Assert.assertEquals(obj.intervalOccurence(new int[]{3,5,7,8,10}, new int[] {1,6,15}), new int[]{2, 3});
    }
}