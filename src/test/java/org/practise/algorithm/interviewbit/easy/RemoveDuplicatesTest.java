package org.practise.algorithm.interviewbit.easy;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class RemoveDuplicatesTest {
    private RemoveDuplicatesII obj = new RemoveDuplicatesII();

    @Test
    public void testRemoveDuplicates() {
        final ArrayList<Integer> inputList = new ArrayList<>(Arrays.asList(0, 0, 0, 1, 1, 2, 2, 3));
        obj.removeDuplicates(inputList);

        Assert.assertEquals(inputList, new ArrayList<>(Arrays.asList(0, 0, 1, 1, 2, 2, 3)));
    }

    @Test
    public void testRemoveDuplicates2() {
        final ArrayList<Integer> inputList = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 2, 3, 3, 3 ));
        obj.removeDuplicates(inputList);

        Assert.assertEquals(inputList, new ArrayList<>(Arrays.asList(0, 0, 1, 1, 2, 3, 3)));
    }
}