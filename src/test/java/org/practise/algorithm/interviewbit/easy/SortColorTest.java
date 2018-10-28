package org.practise.algorithm.interviewbit.easy;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class SortColorTest {
    private SortColor color = new SortColor();

    @Test
    public void testSortColor() {
        final List<Integer> unsortedList = Arrays.asList(0, 1, 2, 0, 1, 2);
        color.sortColors(unsortedList);
        Assert.assertEquals(unsortedList, Arrays.asList(0, 0, 1, 1, 2, 2));
    }
}