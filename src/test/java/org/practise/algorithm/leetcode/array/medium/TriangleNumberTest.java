package org.practise.algorithm.leetcode.array.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class TriangleNumberTest {
    private TriangleNumber triangleNumber = new TriangleNumber();
    @Test
    public void testTriangleNumber() {
        Assert.assertEquals(triangleNumber.triangleNumber(new int[] {0, 0, 0, 7}), 0);
    }
}