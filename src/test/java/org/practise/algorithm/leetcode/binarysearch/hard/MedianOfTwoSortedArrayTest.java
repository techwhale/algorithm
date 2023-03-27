package org.practise.algorithm.leetcode.binarysearch.hard;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.*;

public class MedianOfTwoSortedArrayTest {

    private MedianOfTwoSortedArray obj = new MedianOfTwoSortedArray();

    @Test
    public void testMedianOfTwoSortedArray() {
        int[] nums1 = {};
        int[] nums2 = {20};

        final double medianSortedArrays = obj.findMedianSortedArrays(nums1, nums2);
        Assert.assertEquals(medianSortedArrays, 20.0);
    }

    @Test
    public void testMedianOfTwoSortedArray2() {
        int[] nums1 = {};
        int[] nums2 = {20, 21};

        final double medianSortedArrays = obj.findMedianSortedArrays(nums1, nums2);
        Assert.assertEquals(medianSortedArrays, 20.5);
    }

    @Test
    public void testMedianOfTwoSortedArray3() {
        int[] nums1 = {-40, -25, 5, 10, 14, 28, 29, 48};
        int[] nums2 = {-48, -31, -15, -6, 1, 8};
        // -48 -40 -31 -25 -15 -6 1 5 8 10 14 28 29 48
        final double medianSortedArrays = obj.findMedianSortedArrays(nums1, nums2);
        Assert.assertEquals(medianSortedArrays, 3.0);
    }
}