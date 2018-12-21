package org.practise.algorithm.interviewbit.dynamicprogramming;

import org.testng.Assert;
import org.testng.annotations.Test;

public class NDigitNumbersWithDigitSumSTest {
    private NDigitNumbersWithDigitSumS obj = new NDigitNumbersWithDigitSumS();

    @Test
    public void testNDigitNumbersWithDigitSumS() {
        final int possibilities = obj.solve(2, 5);
        Assert.assertEquals(possibilities, 5);
    }
}