package org.practise.algorithm.leetcode.interestingsolution;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class DailyTemperaturesTest {
    private DailyTemperatures obj = new DailyTemperatures();
    @Test
    public void testDailyTemperatures() {
        int[] temperatures = {73,74,75,71,69,72,76,73};
        int[] actual = obj.dailyTemperatures(temperatures);
        int[] expected = new int[] {1,1,4,2,1,1,0,0};
        Assert.assertEquals(actual, expected);
    }
}