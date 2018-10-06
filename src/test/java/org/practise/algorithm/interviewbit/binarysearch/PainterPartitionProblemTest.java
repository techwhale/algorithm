package org.practise.algorithm.interviewbit.binarysearch;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PainterPartitionProblemTest {
    PainterPartitionProblem painterProblem = new PainterPartitionProblem();

    @Test
    public void testPainter() {
        int[] C = new int[] {1, 10};
        int paint = painterProblem.paint(2, 5, C);
        Assert.assertEquals(paint, 50);
    }
}