package org.practise.algorithm.leetcode.bitmanipulation.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class BitwiseORsofSubarraysTest {
    private BitwiseORsofSubarrays obj = new BitwiseORsofSubarrays();

    @Test
    public void testBitwiseORsofSubarrays1() {
        int result = obj.subarrayBitwiseORs(new int[]{1, 1, 2});
        Assert.assertEquals(result, 3);
    }

    @Test
    public void testBitwiseORsofSubarrays2() {
        int result = obj.subarrayBitwiseORs(new int[]{1});
        Assert.assertEquals(result, 1);
    }

    @Test
    public void testBitwiseORsofSubarrays3() {
        int result = obj.subarrayBitwiseORs(new int[]{1,2,4});
        Assert.assertEquals(result, 6);
    }
}