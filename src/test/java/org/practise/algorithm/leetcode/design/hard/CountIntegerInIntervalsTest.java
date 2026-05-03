package org.practise.algorithm.leetcode.design.hard;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class CountIntegerInIntervalsTest {

    private CountIntegerInIntervals obj = new CountIntegerInIntervals();
    @Test
    public void testAdd() {
        obj.add(2, 3);
        obj.add(7, 10);
        Assert.assertEquals(obj.count(), 6);
        obj.add(5, 8);
        Assert.assertEquals(obj.count(), 8);
    }
}