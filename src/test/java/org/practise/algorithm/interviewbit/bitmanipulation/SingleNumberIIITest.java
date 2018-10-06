package org.practise.algorithm.interviewbit.bitmanipulation;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class SingleNumberIIITest {
    SingleNumberIII obj = new SingleNumberIII();

    @Test
    public void testSingleNumber() {
        int[] nums = {1,2,1,3,2,5};
        int[] result = obj.singleNumber(nums);
        Assert.assertEquals(result, new int[] {5, 3});
    }
}