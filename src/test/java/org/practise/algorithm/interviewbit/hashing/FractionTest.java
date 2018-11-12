package org.practise.algorithm.interviewbit.hashing;

import org.testng.Assert;
import org.testng.annotations.Test;

public class FractionTest {
    private Fraction obj = new Fraction();

    @Test
    public void testFraction() {
        final String result = obj.fractionToDecimal(Integer.MIN_VALUE, -1);
        Assert.assertEquals(result, "2147483648");
    }
}