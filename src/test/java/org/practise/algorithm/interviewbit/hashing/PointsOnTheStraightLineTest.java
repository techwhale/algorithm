package org.practise.algorithm.interviewbit.hashing;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;

public class PointsOnTheStraightLineTest {
    private PointsOnTheStraightLine obj = new PointsOnTheStraightLine();

    @Test
    public void testPointsOnTheStraightLine() {
        final int maxPoints = obj.maxPoints(Arrays.asList(-6, 5, -18, 2, 5, -2), Arrays.asList(-17, -16, -17, -4, -13, 20));
        Assert.assertEquals(maxPoints, 2);
    }
}