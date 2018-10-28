package org.practise.algorithm.interviewbit.binarysearch;

import org.practise.algorithm.interviewbit.binarysearch.PowerFunction;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PowerFunctionTest {
    private PowerFunction powerFunction = new PowerFunction();

    @Test
    public void testPowerFunction() {
        final int pow = powerFunction.pow(3, 3, 5);
        Assert.assertEquals(pow, 2);
    }

    @Test
    public void testPowerFunction2() {
        final int pow = powerFunction.pow(3, 4, 5);
        Assert.assertEquals(pow, 1);
    }


    @Test
    public void testPowerFunction3() {
        final int pow = powerFunction.pow(-1, 1, 20);
        Assert.assertEquals(pow, 19);
    }
}