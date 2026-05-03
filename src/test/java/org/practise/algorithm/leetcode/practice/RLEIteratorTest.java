package org.practise.algorithm.leetcode.practice;

import org.testng.Assert;
import org.testng.annotations.Test;

public class RLEIteratorTest {

    @Test
    public void testRLEIterator1() {
        final int[] initialvalue = {3, 8, 0, 9, 2, 5};
        RLEIterator iterator = new RLEIterator(initialvalue);
        Assert.assertEquals(iterator.next(2), 8);
        Assert.assertEquals(iterator.next(1), 8);
        Assert.assertEquals(iterator.next(1), 5);
        Assert.assertEquals(iterator.next(2), -1);
        Assert.assertEquals(iterator.next(2), -1);
    }

    @Test
    public void testRLEIterator2() {
        final int[] initialvalue = {784, 303, 477, 583, 909, 505};
        RLEIterator iterator = new RLEIterator(initialvalue);
        Assert.assertEquals(iterator.next(130), 303);
        Assert.assertEquals(iterator.next(333), 303);
        Assert.assertEquals(iterator.next(238), 303);
        Assert.assertEquals(iterator.next(87), 583);
        Assert.assertEquals(iterator.next(301), 583);
        Assert.assertEquals(iterator.next(276), 505);
    }
}