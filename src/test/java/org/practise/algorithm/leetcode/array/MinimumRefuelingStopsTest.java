package org.practise.algorithm.leetcode.array;

import org.testng.Assert;
import org.testng.annotations.Test;

public class MinimumRefuelingStopsTest {
    MinimumRefuelingStops minimumRefuelingStops = new MinimumRefuelingStops();
    @Test
    public void testminimumRefuelingStops(){
        int target = 100;
        int initialFuel = 25;
        int[][] stations = new int[][] {
                {25, 25},
                {50, 25},
                {75, 25}
        };
        int stops = minimumRefuelingStops.minRefuelStops(target, initialFuel, stations);
        Assert.assertEquals(stops, 3);
    }

    @Test
    public void testminimumRefuelingStopsMoreTestCases(){
        int target = 1000;
        int initialFuel = 299;
        int[][] stations = new int[][]{
                {13,21},{26,115},{100,47},{225,99},{299,141},{444,198},{608,190},{636,157},{647,255},{841,123}
        };
        int stops = minimumRefuelingStops.minRefuelStops(target, initialFuel, stations);
        Assert.assertEquals(stops, 4);
    }
}