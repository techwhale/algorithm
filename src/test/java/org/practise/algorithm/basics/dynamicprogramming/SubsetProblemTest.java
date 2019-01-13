package org.practise.algorithm.basics.dynamicprogramming;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SubsetProblemTest {
    private SubsetProblem obj = new SubsetProblem();

    @Test
    public void testSubsetProblem() {
        Assert.assertTrue(obj.subsetExist(new int[]{2, 7, 10, 3, 8}, 11));
    }
}