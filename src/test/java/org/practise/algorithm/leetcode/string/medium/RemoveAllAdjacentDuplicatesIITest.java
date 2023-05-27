package org.practise.algorithm.leetcode.string.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class RemoveAllAdjacentDuplicatesIITest {
    RemoveAllAdjacentDuplicatesII obj = new RemoveAllAdjacentDuplicatesII();
    @Test
    public void testRemoveDuplicates() {
        Assert.assertEquals(obj.removeDuplicates("abcd", 2), "abcd");
        Assert.assertEquals(obj.removeDuplicates("deeedbbcccbdaa", 3), "aa");
        Assert.assertEquals(obj.removeDuplicates("pbbcggttciiippooaais", 2), "ps");
    }
}