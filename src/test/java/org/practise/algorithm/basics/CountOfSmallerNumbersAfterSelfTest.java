package org.practise.algorithm.basics;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

public class CountOfSmallerNumbersAfterSelfTest {
    private  CountOfSmallerNumbersAfterSelf obj = new CountOfSmallerNumbersAfterSelf();
    @Test
    public void testCountSmaller() {
        int[] arr = new int[] {5,2,6,1};
        List<Integer> actual = obj.countSmaller(arr);
        Assert.assertEquals(actual, List.of(2,1,1,0));
    }
}