package org.practise.algorithm.leetcode.array.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ProductOfArrayExceptSelfTest {
    private ProductOfArrayExceptSelf obj = new ProductOfArrayExceptSelf();

    @Test
    public void testProductOfArrayExceptSelf() {
        final int[] nums = {1, 2, 3, 4};
        Assert.assertEquals(obj.productExceptSelf(nums), new int[] {24,12,8,6});
    }
}