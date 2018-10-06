package org.practise.algorithm.leetcode.linkedlist;

import org.testng.Assert;
import org.testng.annotations.Test;


public class LRUCacheTest {
    @Test
    public void testLRUCache() {
        LRUCache cache = new LRUCache(2);

        cache.put(1,1);
        cache.put(2,2);

        int result = cache.get(1);
        Assert.assertEquals(result, 1);

        cache.put(3,3);

        result = cache.get(2);
        Assert.assertEquals(result, -1);

        cache.put(4,4);

        result = cache.get(1);
        Assert.assertEquals(result, -1);

        result = cache.get(3);
        Assert.assertEquals(result, 3);

        result = cache.get(4);
        Assert.assertEquals(result, 4);
    }
}