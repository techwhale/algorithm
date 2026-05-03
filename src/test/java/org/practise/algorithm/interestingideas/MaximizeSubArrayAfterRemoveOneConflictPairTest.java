package org.practise.algorithm.interestingideas;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class MaximizeSubArrayAfterRemoveOneConflictPairTest {

    private MaximizeSubArrayAfterRemoveOneConflictPair pair = new MaximizeSubArrayAfterRemoveOneConflictPair();

    @Test
    public void testTestMaxSubarrays() {
        Assert.assertEquals(pair.maxSubarrays(4, new int[][]{{2,3}, {1,4}}), 9);
    }
}