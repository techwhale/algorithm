package org.practise.algorithm.leetcode.array.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ShortestSubarraySumatLeastKTest {
    ShortestSubarraySumatLeastK obj = new ShortestSubarraySumatLeastK();

    @Test
    public void test1() {
        int[] arr = new int[] {1};
        int result = obj.shortestSubarray(arr, 1);
        Assert.assertEquals(result, 1);
    }

    @Test
    public void test2() {
        int[] arr = new int[] {1,2};
        int result = obj.shortestSubarray(arr, 4);
        Assert.assertEquals(result, -1);
    }

    @Test
    public void test3() {
        int[] arr = new int[] {2,-1,2};
        int result = obj.shortestSubarray(arr, 3);
        Assert.assertEquals(result, 3);
    }

    @Test
    public void cornerCase() {
        int[] arr = new int[] {84,-37,32,40,95};
        int result = obj.shortestSubarray(arr, 167);
        Assert.assertEquals(result, 3);

    }
}