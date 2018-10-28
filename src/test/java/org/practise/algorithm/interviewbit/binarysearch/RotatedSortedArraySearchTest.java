package org.practise.algorithm.interviewbit.binarysearch;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class RotatedSortedArraySearchTest {
    private RotatedSortedArraySearch obj = new RotatedSortedArraySearch();

    @Test
    public void testRotatedSortedArraySearch() {
        final ArrayList<Integer> arrayList = new ArrayList<>(Arrays.asList(4, 5, 6, 7, 8, 0, 2, 3));
        final int index = obj.search(arrayList, 2);
        Assert.assertEquals(index, 6);
    }

    @Test
    public void testRotatedSortedArraySearch2() {
        final ArrayList<Integer> arrayList = new ArrayList<>(Arrays.asList(4, 5, 6, 7, 8, 0, 2, 3));
        final int index = obj.search(arrayList, 5);
        Assert.assertEquals(index, 1);
    }


    @Test
    public void testRotatedSortedArraySearch3() {
        final ArrayList<Integer> arrayList = new ArrayList<>(Arrays.asList(4, 5, 6, 7, 8, 0, 2, 3));
        final int index = obj.search(arrayList, 8);
        Assert.assertEquals(index, 4);
    }


    @Test
    public void testRotatedSortedArraySearch4() {
        final ArrayList<Integer> arrayList = new ArrayList<>(Arrays.asList(4, 5, 6, 7, 8, 0, 2, 3));
        final int index = obj.search(arrayList, 1);
        Assert.assertEquals(index, -1);
    }
}