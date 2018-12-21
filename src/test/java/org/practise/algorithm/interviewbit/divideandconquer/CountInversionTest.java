package org.practise.algorithm.interviewbit.divideandconquer;

import org.testng.Assert;
import org.testng.annotations.Test;

public class CountInversionTest {
    private CountInversion inversion = new CountInversion();

    @Test
    public void testCountInversion() {
        final int result = inversion.countInversions(new int[]{2, 4, 1, 3, 5});
        Assert.assertEquals(result, 3);
    }

    @Test
    public void testCountInversion2() {
        final int result = inversion.countInversions(new int[]{59, 29});
        Assert.assertEquals(result, 1);
    }

    @Test
        public void testCountInversion3() {
        final int result = inversion.countInversions(
                new int[]{84, 2, 37, 3, 67, 82, 19, 97, 91, 63, 27, 6, 13, 90, 63, 89, 100, 60, 47, 96, 54, 26, 64, 50, 71, 16, 6, 40, 84, 93, 67, 85, 16, 22, 60});
        Assert.assertEquals(result, 290);
    }
}