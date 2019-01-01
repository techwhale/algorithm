package org.practise.algorithm.interestingideas;

import org.practise.algorithm.interestingideas.OutOfBoundaryPaths;
import org.testng.Assert;
import org.testng.annotations.Test;

public class OutOfBoundaryPathsTest {
    private OutOfBoundaryPaths obj = new OutOfBoundaryPaths();

    @Test
    public void testOutOfBoundaryPaths() {
        Assert.assertEquals(obj.findPaths(3, 2, 5, 1, 1), 109);
    }
}