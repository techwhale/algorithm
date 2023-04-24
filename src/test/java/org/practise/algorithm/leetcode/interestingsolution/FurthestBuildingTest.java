package org.practise.algorithm.leetcode.interestingsolution;

import org.practise.algorithm.leetcode.interestingsolution.FurthestBuilding;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FurthestBuildingTest {
    FurthestBuilding obj = new FurthestBuilding();
    @Test
    public void testFurthestBuilding() {
        int[] heights = {4,2,7,6,9,14,12};
        Assert.assertEquals(obj.furthestBuilding(heights, 5, 1), 4);
    }
}