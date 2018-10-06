package org.practise.algorithm.interviewbit.bitmanipulation;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.*;

public class SingleNumberIITest {
    SingleNumberII obj = new SingleNumberII();

    @Test
    public void singleNumberTest() {
        int[] arr = {1, 2, 4, 3, 3, 2, 2, 3, 1, 1};
        int singleNumber = obj.singleNumber(arr);
        Assert.assertEquals(singleNumber, 4);
    }
}