package org.practise.algorithm.leetcode.interestingsolution;

import org.practise.algorithm.interestingideas.RemoveMostStonesWithSameRowOrColumn;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RemoveMostStonesWithSameRowOrColumnTest {
    private RemoveMostStonesWithSameRowOrColumn obj = new RemoveMostStonesWithSameRowOrColumn();

    @Test
    public void testRemoveMostStonesWithSameRowOrColumn() {
        int[][] stones = new int[][] {{ 0,1 }, {1,0 },{1,1}};
        final int removedStones = obj.removeStones(stones);
        Assert.assertEquals(removedStones, 2);
    }
}