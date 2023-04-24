package org.practise.algorithm.leetcode.backtracking;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class MaxAcceptedInvitationsTest {

    private MaxAcceptedInvitations obj = new MaxAcceptedInvitations();
    @Test
    public void testMaximumInvitations() {
        int[][] grid = {{1,0,1,0}, {1,0,0,0}, {0,0,1,0}, {1,1,1,0}};
        Assert.assertEquals(obj.maximumInvitations(grid), 3);
    }
}