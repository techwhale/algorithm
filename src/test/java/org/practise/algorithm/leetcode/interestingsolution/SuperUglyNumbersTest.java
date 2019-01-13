package org.practise.algorithm.leetcode.interestingsolution;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SuperUglyNumbersTest {
    private SuperUglyNumbers obj = new SuperUglyNumbers();

    @Test
    public void testSuperUglyNumbers() {
        Assert.assertEquals(obj.nthSuperUglyNumber(6, new int[] {2, 3, 5}), 6);
    }
}