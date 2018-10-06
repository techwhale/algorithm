package org.practise.algorithm.leetcode.array.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PartitionEqualSubsetSumTest {
    PartitionEqualSubsetSum obj = new PartitionEqualSubsetSum();

    @Test
    public void testPartitionSubset() {
        int[] arr = new int[] {1, 5, 11, 5};
        boolean partition = obj.canPartition(arr);
        Assert.assertEquals(partition, true);
    }
}