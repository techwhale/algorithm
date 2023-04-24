package org.practise.algorithm.leetcode.design.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class MRUQueueTest {
    @Test
    public void testFetch() {
        MRUQueue queue = new MRUQueue(8);
        Assert.assertEquals(queue.fetch(3), 3);
        Assert.assertEquals(queue.fetch(5), 6);
        Assert.assertEquals(queue.fetch(2), 2);
        Assert.assertEquals(queue.fetch(8), 2);
    }
}