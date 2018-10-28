package org.practise.algorithm.leetcode.interestingsolution;

import org.practise.algorithm.leetcode.interestingsolution.FlipStringToMonotoneIncreasing;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FlipStringToMonotoneIncreasingTest {
    private FlipStringToMonotoneIncreasing obj = new FlipStringToMonotoneIncreasing();

    @Test
    public void testFlipStringToMonotoneIncreasing() {
        Assert.assertEquals(obj.minFlipsMonoIncr("00110"), 1);
    }

    @Test
    public void testFlipStringToMonotoneIncreasing2() {
        Assert.assertEquals(obj.minFlipsMonoIncr("010110"), 2);
    }

    @Test
    public void testFlipStringToMonotoneIncreasing3() {
        Assert.assertEquals(obj.minFlipsMonoIncr("00011000"), 2);
    }

    @Test
    public void testFlipStringToMonotoneIncreasing4() {
        Assert.assertEquals(obj.minFlipsMonoIncr("00011001"), 2);
    }

    @Test
    public void testFlipStringToMonotoneIncreasing5() {
        Assert.assertEquals(obj.minFlipsMonoIncr("1111001110"), 3);
    }

    @Test
    public void testFlipStringToMonotoneIncreasing6() {
        Assert.assertEquals(obj.minFlipsMonoIncr("10"), 1);
    }
}