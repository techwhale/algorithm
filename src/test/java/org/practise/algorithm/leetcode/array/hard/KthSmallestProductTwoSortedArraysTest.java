package org.practise.algorithm.leetcode.array.hard;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class KthSmallestProductTwoSortedArraysTest {

    private KthSmallestProductTwoSortedArrays obj = new KthSmallestProductTwoSortedArrays();

    @Test
    public void testKthSmallestProduct() {
        int[] nums1 = {-4, -2, 0, 3};
        int[] nums2 = {2, 4};
        Assert.assertEquals(obj.kthSmallestProduct(nums1, nums2, 6), 0);
    }
}