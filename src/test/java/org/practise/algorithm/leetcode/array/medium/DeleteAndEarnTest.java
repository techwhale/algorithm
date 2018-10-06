package org.practise.algorithm.leetcode.array.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class DeleteAndEarnTest {
    DeleteAndEarn obj = new DeleteAndEarn();
    @Test
    public void testDeleteAndEarn() {
        int[] elem = new int[] {2, 2, 3, 3, 3, 4};
        int maxEarn = obj.deleteAndEarn(elem);
        Assert.assertEquals(maxEarn, 9);
    }
}