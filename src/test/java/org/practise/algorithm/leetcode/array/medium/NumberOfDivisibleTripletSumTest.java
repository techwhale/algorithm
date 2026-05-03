package org.practise.algorithm.leetcode.array.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class NumberOfDivisibleTripletSumTest {

    NumberOfDivisibleTripletSum obj = new NumberOfDivisibleTripletSum();
    @Test
    public void testDivisibleTripletCount() {
        int[] values = new int[] {3,3,4,7,8};
        Assert.assertEquals(obj.divisibleTripletCount(values, 5), 3);
    }
}