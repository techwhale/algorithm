package org.practise.algorithm.leetcode.array.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class PartitionDisjointedTest {
    PartitionDisjointed obj = new PartitionDisjointed();

    @Test
    public void testPartitionDisjointed1() {
        int[] val = new int[] {5,0,3,8,6};
        int size = obj.partitionDisjoint(val);
        Assert.assertEquals(size, 3);
    }

    @Test
    public void testPartitionDisjointed2() {
        int[] val = new int[] {1,1,1,0,6,12};
        int size = obj.partitionDisjoint(val);
        Assert.assertEquals(size, 4);
    }

    @Test
    public void testPartitionDisjointed3() {
        int[] val = new int[] {3, 0, 8, 3, 12};
        int size = obj.partitionDisjoint(val);
        Assert.assertEquals(size, 2);
    }
}