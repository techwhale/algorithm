package org.practise.algorithm.leetcode.interestingsolution;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class RangeSumQuery1DTest {
    @Test
    public void testRangeSumQuery1D() {
        int[] values = {1, 3, 5};
        RangeSumQuery1D obj = new RangeSumQuery1D(values);
        Assert.assertEquals(obj.sumRange(0, 2), 9);
        obj.update(1, 2);
        Assert.assertEquals(obj.sumRange(0, 2), 8);
    }

    @Test
    public void testRange() {
        int times = 5;
        int value = 5;
        while (times > 0) {
            System.out.println("value = " +  value + " Binary format  =" + Integer.toBinaryString(value));
            System.out.println("value = " +  -value + " Binary format  =" + Integer.toBinaryString(-value));
            System.out.println("value & - value = " +  (value & -value) + " Binary format  =" + Integer.toBinaryString(value & -value));
            System.out.println("value - (value & - value) = " +  (value - (value & -value)) + " Binary format  =" + Integer.toBinaryString(value - (value & -value)));
            System.out.println("value + (value & - value) = " +  (value + (value & -value)) + " Binary format  =" + Integer.toBinaryString(value + (value & -value)));
            times--;
            value = value + (value & -value);
        }
    }
}