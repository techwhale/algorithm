package org.practise.algorithm.leetcode.array.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class UnHappyFriendsTest {

    @Test
    public void testUnhappyFriends() {
        UnHappyFriends obj = new UnHappyFriends();
        int[][] preferences = {{1, 2, 3}, {3, 2, 0}, {3, 1, 0}, {1, 2, 0}};
        int[][] pairs = {{0, 1}, {2, 3}};
        Assert.assertEquals(obj.unhappyFriends(4, preferences, pairs), 2);
    }
}