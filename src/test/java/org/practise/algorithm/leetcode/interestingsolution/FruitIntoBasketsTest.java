package org.practise.algorithm.leetcode.interestingsolution;

import org.practise.algorithm.leetcode.interestingsolution.FruitIntoBaskets;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FruitIntoBasketsTest {
    private FruitIntoBaskets obj = new FruitIntoBaskets();

    @Test
    public void testFruitIntoBaskets() {
        final int[] fruits = {0, 1, 2, 2};
        Assert.assertEquals(obj.totalFruit(fruits), 3);
    }

    @Test
    public void testFruitIntoBaskets2() {
        final int[] fruits = {0};
        Assert.assertEquals(obj.totalFruit(fruits), 1);
    }

    @Test
    public void testFruitIntoBaskets3() {
        final int[] fruits = {1,0,1,4,1,4,1,2,3};
        Assert.assertEquals(obj.totalFruit(fruits), 5);
    }
}