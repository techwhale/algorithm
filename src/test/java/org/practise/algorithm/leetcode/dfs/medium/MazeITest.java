package org.practise.algorithm.leetcode.dfs.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

public class MazeITest {
    private MazeI obj = new MazeI();

    @Test
    public void testMazeI() {
        int[][] paths = {{0, 0, 1, 0, 0}, {0, 0 ,0 ,0, 0}, {0, 0, 0, 1, 0}, {1, 1, 0, 1, 1}, {0, 0, 0, 0, 0}};
        final boolean pathExist = obj.hasPath(paths, new int[]{0, 4}, new int[]{4, 4});
        Assert.assertEquals(pathExist, true);
    }
}