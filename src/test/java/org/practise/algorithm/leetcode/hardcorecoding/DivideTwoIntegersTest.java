package org.practise.algorithm.leetcode.hardcorecoding;

import org.testng.Assert;
import org.testng.annotations.Test;

public class DivideTwoIntegersTest {
    private DivideTwoIntegers obj = new DivideTwoIntegers();

    @Test
    public void testDivideTwoIntegers() {
        final int result = obj.divide(10, 3);
        Assert.assertEquals(result, 3);
    }
}