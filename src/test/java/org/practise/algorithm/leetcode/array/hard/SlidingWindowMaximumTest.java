package org.practise.algorithm.leetcode.array.hard;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class SlidingWindowMaximumTest {

    public static void main(String[] args) {
        SlidingWindowMaximum obj = new SlidingWindowMaximum();
        int[] expected = new int[] {1,-1};
        int[] actual = obj.maxSlidingWindow(new int[] {1,-1}, 1);
    }

//    @Test
//    public void testMaxSlidingWindow() {
//        int[] expected = new int[] {3,3,5,5,6,7};
//        int[] actual = obj.maxSlidingWindow(new int[] {1,3,-1,-3,5,3,6,7}, 3);
//        Assert.assertEquals(actual, expected);
//    }
}