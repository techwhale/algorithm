package org.practise.algorithm.leetcode.array.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

public class OneEditDistanceTest {
    private OneEditDistance obj = new OneEditDistance();

    @Test
    public void testOneEditDistance() {
        Assert.assertTrue(obj.isOneEditDistance("ae", "ace"));
    }
}