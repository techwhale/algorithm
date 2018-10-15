package org.practise.algorithm.leetcode.practice;

import org.testng.Assert;
import org.testng.annotations.Test;

public class RLEIteratorTest {

    @Test
    public void testRLEIterator() {
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