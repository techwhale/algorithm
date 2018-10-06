package org.practise.algorithm.interviewbit.binarysearch;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class SquareRootTest {
    SquareRoot squareRoot = new SquareRoot();
    @Test
    public void testSquareRoot() {
        int sqrt = squareRoot.sqrt(2147483647);
        Assert.assertEquals(sqrt, 46340);

        sqrt = squareRoot.sqrt(2);
        Assert.assertEquals( sqrt, 1);
    }
}