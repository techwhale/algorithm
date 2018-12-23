package org.practise.algorithm.basics.dynamicprogramming;

import org.testng.Assert;
import org.testng.annotations.Test;

public class KnapsackTest {
    private Knapsack obj = new Knapsack();

    @Test
    public void testKnapsack() {
        int[] weight = new int[] {1, 3, 4, 5};
        int[] value = new int[] {1, 4, 5, 7};
        Assert.assertEquals(obj.knapsack(weight, value, 7), 9);
    }
}