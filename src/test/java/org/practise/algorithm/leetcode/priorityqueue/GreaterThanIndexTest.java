package org.practise.algorithm.leetcode.priorityqueue;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class GreaterThanIndexTest {
    private GreaterThanIndex obj = new GreaterThanIndex();
    @Test
    public void testGreaterThanIndex() {
        int[] actual = obj.greaterThanIndex(new int[] {21,5,6,56,88,52});
        int[] expected = new int[] {5,5,5,4,-1,-1};
        Assert.assertEquals(actual, expected);
    }
}