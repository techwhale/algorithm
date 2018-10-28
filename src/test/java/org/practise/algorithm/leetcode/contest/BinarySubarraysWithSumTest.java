package org.practise.algorithm.leetcode.contest;

import org.practise.algorithm.leetcode.array.BinarySubarraysWithSum;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BinarySubarraysWithSumTest {
    private BinarySubarraysWithSum obj = new BinarySubarraysWithSum();

    @Test
    public void testBinarySubarraysWithSum() {
        final int[] ints = {1, 0, 1, 0, 1};
        Assert.assertEquals(obj.numSubarraysWithSum(ints, 2), 4);
    }


    @Test
    public void testBinarySubarraysWithSum2() {
        final int[] ints = {0, 0, 0, 0, 1};
        Assert.assertEquals(obj.numSubarraysWithSum(ints, 2), 0);
    }


    @Test
    public void testBinarySubarraysWithSum3() {
        final int[] ints = {1, 0, 1, 0, 1};
        Assert.assertEquals(obj.numSubarraysWithSum(ints, 1), 8);
    }
}