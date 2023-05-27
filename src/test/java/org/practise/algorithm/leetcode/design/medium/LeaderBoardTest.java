package org.practise.algorithm.leetcode.design.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class LeaderBoardTest {
    @Test
    public void testDesign(){
        LeaderBoard leaderBoard = new LeaderBoard();
        leaderBoard.addScore(1, 73);
        leaderBoard.addScore(2, 56);
        leaderBoard.addScore(3, 39);
        leaderBoard.addScore(4, 51);
        leaderBoard.addScore(5, 4);
        Assert.assertEquals(leaderBoard.top(1), 73);
        Assert.assertEquals(leaderBoard.top(2), 129);
        leaderBoard.reset(1);
        leaderBoard.reset(2);
        leaderBoard.addScore(2, 51);
        Assert.assertEquals(leaderBoard.top(3), 141);
    }
}