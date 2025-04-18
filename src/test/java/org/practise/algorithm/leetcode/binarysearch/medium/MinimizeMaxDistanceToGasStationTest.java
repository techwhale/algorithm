package org.practise.algorithm.leetcode.binarysearch.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class MinimizeMaxDistanceToGasStationTest {
    private MinimizeMaxDistanceToGasStation obj = new MinimizeMaxDistanceToGasStation();
    @Test
    public void testMinmaxGasDist() {
        int[] station = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        final double v = obj.minmaxGasDist(station, 9);
        Assert.assertTrue(v >= 0.48999968);
    }

    @Test
    public void testMinmaxGasDist2() {
        int[] station = new int[] {23,24,36,39,46,56,57,65,84,98};
        final double v = obj.minmaxGasDist2(station, 1);
        Assert.assertTrue(v >= 14);
    }
}