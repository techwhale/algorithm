package org.practise.algorithm.interviewbit.easy;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.testng.Assert.*;

public class ThreeSumZeroTest {
    private ThreeSumZero obj = new ThreeSumZero();

    @Test
    public void testThreeSumZero() {
        final ArrayList<ArrayList<Integer>> threeSum = obj.threeSum(Arrays.asList(-1, 0, 1, 2, -1, -4));
        Assert.assertEquals(threeSum.size(), 2);
    }

    @Test
    public void testThreeSumZero2() {
        final ArrayList<ArrayList<Integer>> threeSum = obj.threeSum(Arrays.asList(-31013930, -31013930, 9784175, 21229755));
        Assert.assertEquals(threeSum.size(), 1);
    }

}