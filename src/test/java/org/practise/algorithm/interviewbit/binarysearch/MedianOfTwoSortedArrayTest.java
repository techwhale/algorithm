package org.practise.algorithm.interviewbit.binarysearch;

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
        List<Integer> list1 = Collections.EMPTY_LIST;
        List<Integer> list2 = Arrays.asList(20);

        final double medianSortedArrays = obj.findMedianSortedArrays(list1, list2);
        Assert.assertEquals(medianSortedArrays, 20.0);
    }

    @Test
    public void testMedianOfTwoSortedArray2() {
        List<Integer> list1 = Collections.EMPTY_LIST;
        List<Integer> list2 = Arrays.asList(20, 21);

        final double medianSortedArrays = obj.findMedianSortedArrays(list1, list2);
        Assert.assertEquals(medianSortedArrays, 20.5);
    }

    @Test
    public void testMedianOfTwoSortedArray3() {
        List<Integer> list1 = Arrays.asList(-40, -25, 5, 10, 14, 28, 29, 48);
        List<Integer> list2 = Arrays.asList(-48, -31, -15, -6, 1, 8);
        // -48 -40 -31 -25 -15 -6 1 5 8 10 14 28 29 48
        final double medianSortedArrays = obj.findMedianSortedArrays(list1, list2);
        Assert.assertEquals(medianSortedArrays, 3.0);
    }
}