package org.practise.algorithm.leetcode.priorityqueue;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class MaximalScoreAfterKOperationTest {

    private MaximalScoreAfterKOperation maximalScoreAfterKOperation = new MaximalScoreAfterKOperation();
    @Test
    public void testMaxKelements() {
        int[] nums = {1, 10, 3, 3, 3};
        Assert.assertEquals(maximalScoreAfterKOperation.maxKelements(nums, 3), 17);
        int[] nums2 = {672579538,806947365,854095676,815137524};
        Assert.assertEquals(maximalScoreAfterKOperation.maxKelements(nums2, 3), 2476180565l);
    }
}