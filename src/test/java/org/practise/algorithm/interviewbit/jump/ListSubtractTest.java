package org.practise.algorithm.interviewbit.jump;

import org.practise.algorithm.pojo.ListNode;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.practise.algorithm.util.TestUtil.convertLinkedListToList;
import static org.practise.algorithm.util.TestUtil.createListNode;

public class ListSubtractTest {
    private ListSubtract obj = new ListSubtract();

    @Test
    public void testListSubtract() {
        final int[] vals = {1, 2, 3, 4, 5};
        final ListNode listNode = createListNode(vals);
        final ListNode subtract = obj.subtract(listNode);

        final List<Integer> result = convertLinkedListToList(subtract);
        final List<Integer> expectedList = new ArrayList<>(Arrays.asList(4, 2, 3, 4, 5));
        Assert.assertEquals(result, expectedList);
    }

    @Test
    public void testListSubtract2() {
        final int[] vals = {0, 1, 2, 3, 4, 5};
        final ListNode listNode = createListNode(vals);
        final ListNode subtract = obj.subtract(listNode);

        final List<Integer> result = convertLinkedListToList(subtract);
        final List<Integer> expectedList = new ArrayList<>(Arrays.asList(5, 3, 1, 3, 4, 5));
        Assert.assertEquals(result, expectedList);
    }


    @Test
    public void testListSubtract3() {
        final int[] vals = {95, 59, 26, 16, 31, 39, 29, 8, 74, 14, 41, 41, 64, 88, 34, 21, 67, 23, 17, 41, 3, 38, 4, 45, 93, 92, 99, 24};
        final ListNode listNode = createListNode(vals);
        final ListNode subtract = obj.subtract(listNode);

        final List<Integer> result = convertLinkedListToList(subtract);
        final List<Integer> expectedList = new ArrayList<>(Arrays.asList(-71, 40, 66, 77, 14, -35, 9, -5, -33, 3, -18, 26, -43, -54, 34, 21, 67, 23, 17, 41, 3, 38, 4, 45, 93, 92, 99, 24));
        Assert.assertEquals(result, expectedList);
    }
}