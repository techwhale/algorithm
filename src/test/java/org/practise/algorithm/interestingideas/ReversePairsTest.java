package org.practise.algorithm.interestingideas;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ReversePairsTest {
    private ReversePairs obj = new ReversePairs();

    @Test
    public void testReversePairs() {
        final int[] arr = {1, 3, 2, 3, 1};
        Assert.assertEquals(obj.reversePairs(arr), 2);
    }
}