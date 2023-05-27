package org.practise.algorithm.leetcode.interestingsolution;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class TwoCitySchedulingTest {
    TwoCityScheduling obj = new TwoCityScheduling();
    @Test
    public void testTwoCitySchedCost() {
        int[][] costs = {{259,770},{448,54},{926,667},{184,139},{840,118},{577,469}};
        Assert.assertEquals(obj.twoCitySchedCost(costs), 1859);
    }
}