package org.practise.algorithm.leetcode.hardcorecoding;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class CalculatorIITest {
    private CalculatorII obj = new CalculatorII();

    @Test
    public void testCalculatorII() {
        final int result = obj.calculate(" 3+5 / 2 ");
        Assert.assertEquals(result, 5);
    }
}