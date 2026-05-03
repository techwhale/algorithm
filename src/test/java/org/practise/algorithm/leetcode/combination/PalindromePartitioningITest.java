package org.practise.algorithm.leetcode.combination;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class PalindromePartitioningITest {

    private PalindromePartitioningI obj = new PalindromePartitioningI();
    @Test
    public void testPartition() {
        obj.partition("aab");
    }
}