package org.practise.algorithm.leetcode.dfs.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class BeautifulArrayTest {
    private BeautifulArray obj = new BeautifulArray();

    @Test
    public void testBeautifulArray() {
        final int[] array = obj.beautifulArray(5);
        Assert.assertEquals(array, new int[]{1, 5, 3, 2, 4});
    }

}