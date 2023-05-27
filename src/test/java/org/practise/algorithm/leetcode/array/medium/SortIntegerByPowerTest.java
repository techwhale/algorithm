package org.practise.algorithm.leetcode.array.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class SortIntegerByPowerTest {
    @Test
    public void testGetKth() {
        SortIntegerByPower obj = new SortIntegerByPower();
        Assert.assertEquals(obj.getKth(10, 20, 5), 13);
    }
}